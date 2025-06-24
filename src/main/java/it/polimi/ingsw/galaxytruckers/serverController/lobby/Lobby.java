package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListener;
import it.polimi.ingsw.galaxytruckers.model.GameInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Represents a game lobby where players can gather before starting a game.
 * Manages player connections, game setup, and coordinates game events.
 * Each lobby has a unique ID, a host player, and supports a specified number of players.
 */
public class Lobby implements LobbyInterface {
    private static final long REMOVAL_DELAY = 15000;

    private final Object paramLock = new Object();

    private final UUID id;
    private final Level level;
    private final int numPlayers;
    private final Player host;

    private volatile LobbyState state;
    private final GameInterface game;
    private final List<Player> players;
    private final Set<GameColor> chosenColors;
    private final Map<Player, GameColor> playerColors;

    private EventQueue<LobbyEvent> eventQueue;
    private final LobbyEventHandler lobbyEventHandler;

    private final Set<Player> disconnectedPlayers = new HashSet<>();

    private final Consumer<Lobby> removeLobby;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private long removalDelay = REMOVAL_DELAY;

    /**
     * Creates a new lobby with the specified game, creator, level, and player count.
     * Initializes the lobby with the creator as the host and sets up event handling.
     *
     * @param game the game interface that will manage game state
     * @param creator the player who created this lobby
     * @param level the difficulty level of the game
     * @param numPlayers the maximum number of players allowed in this lobby
     * @param removeLobby callback function to remove the lobby when it's no longer needed
     */
    public Lobby(GameInterface game, Player creator, Level level, int numPlayers, Consumer<Lobby> removeLobby) {
        this.game = game;
        this.id = UUID.randomUUID();
        this.level = level;
        this.numPlayers = numPlayers;
        this.host = creator;
        this.removeLobby = removeLobby;

        this.state = LobbyState.PREPARATION;
        this.players = new ArrayList<>();
        this.chosenColors = new HashSet<>();
        this.playerColors = new HashMap<>();

        this.eventQueue = new EventQueue<>();
        this.lobbyEventHandler = new LobbyEventHandler(this.eventQueue, this);

        lobbyEventHandler.start();
        addPlayer(creator);
    }

    /**
     * Gets the host player of this lobby.
     *
     * @return the player who created this lobby
     */
    public Player getHost() {
        return host;
    }

    /**
     * Gets the unique identifier for this lobby.
     *
     * @return the UUID of this lobby
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the flight level of the game in this lobby.
     *
     * @return the level of the game
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gets the maximum number of players allowed in this lobby.
     *
     * @return the maximum number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Gets a copy of the list of players currently in the lobby.
     *
     * @return a list containing all players in the lobby
     */
    public List<Player> getPlayers() {
        List<Player> res;
        synchronized (paramLock) {
            res = new ArrayList<>(players);
        }
        return res;
    }

    /**
     * Gets a copy of the map associating players with their game colors.
     *
     * @return a map of players to their assigned colors
     */
    public Map<Player, GameColor> getPlayerColors() {
        Map<Player, GameColor> res;
        synchronized (paramLock) {
            res = new HashMap<>(playerColors);
        }
        return res;
    }

    /**
     * Adds a player to the lobby, assigning a color among the remaining ones.
     * If {@link #numPlayers} is not reached, enqueues the joining event,
     * otherwise calls {@link #startGame()}
     *
     * @param player the player to add
     * @return true if adding this player fills the lobby and starts the game, false otherwise
     */
    public boolean addPlayer(Player player) {
        boolean res;
        synchronized (paramLock) {
            checkLobbyState(LobbyState.PREPARATION);
            GameColor chosenColor = Arrays.stream(GameColor.values())
                    .filter(c -> !chosenColors.contains(c))
                    .findAny().orElseThrow();
            chosenColors.add(chosenColor);
            players.add(player);
            playerColors.put(player, chosenColor);
            player.setLobby(this);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
            eventQueue.notifyEvent(new LobbyDetailsEvent(
                    player.getNickname(),
                    DtoConverter.getLobbyDetails(this)
            ));
            eventQueue.notifyEvent(new JoinLobbyEvent(player.getNickname(), playerColors.get(player)));
            if (players.size() == numPlayers) {
                startGame();
                res = true;
            } else {
                res = false;
            }
        }
        return res;
    }

    /**
     * Handles a player disconnection from the lobby.
     * Notifies other players of the disconnection and schedules lobby removal
     * if all players are disconnected.
     *
     * @param player the player who disconnected
     */
    public void notifyPlayerDisconnection(Player player) {
        synchronized (paramLock) {
            if (disconnectedPlayers.add(player)) {
                eventQueue.notifyEvent(new PlayerDisconnectionEvent(player.getNickname()));
                if (disconnectedPlayers.size() == getPlayers().size()) {
                    scheduledFuture = scheduler.schedule(this::remove, removalDelay, TimeUnit.MILLISECONDS);
                    System.out.println("Scheduled lobby " + getId() + " removal");
                }
            }
        }
    }

    /**
     * Handles a player reconnection to the lobby.
     * Cancels any scheduled lobby removal and sends the current game state to the reconnected player.
     *
     * @param player the player who reconnected
     */
    public void notifyPlayerReconnection(Player player) {
        synchronized (paramLock) {
            disconnectedPlayers.remove(player);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
            if (state == LobbyState.INGAME) {
                game.requestSnapshot(player.getShipBoard().orElseThrow());
            } else {
                eventQueue.notifyEvent(new GameSnapshotEvent(
                        player.getNickname(),
                        DtoConverter.getLobbyDetails(this),
                        null
                ));
            }
        }
    }

    /**
     * Notifies other players that a player has left the lobby.
     *
     * @param player the player who left
     */
    public void notifyPlayerExit(Player player) {
        eventQueue.notifyEvent(new PlayerExitEvent(player.getNickname()));
    }

    /**
     * Removes this lobby from the server and stops the event handler.
     * Called when the lobby is no longer needed (e.g., when all players have left).
     */
    public void remove() {
        this.removeLobby.accept(this);
        this.lobbyEventHandler.stop();
    }

    /**
     * Verifies that the lobby is in the expected state.
     * Throws an exception if the current state doesn't match the expected one.
     *
     * @param lobbyState the expected state of the lobby
     * @throws IllegalStateException if the lobby is not in the expected state
     */
    private void checkLobbyState(LobbyState lobbyState) {
        if (state != lobbyState) {
            throw new IllegalStateException("The lobby is not in " + lobbyState.toString());
        }
    }

    /**
     * Sets the current state of the lobby.
     *
     * @param state the new state for the lobby
     */
    public void setState(LobbyState state) {
        this.state = state;
    }

    public LobbyState getState() {
        return state;
    }

    /**
     * Brings the state of the lobby to {@link LobbyState#INGAME}
     * Sets the game of the lobby to a new instance of {@link Game} of the specified level
     */
    private void startGame() {
        for (Player player : getPlayers()) {
            ShipBoard ship = game.addShipBoard(playerColors.get(player));
            player.setShipBoard(ship);
        }
        game.setEventListener(new GameEventListener(this.eventQueue, () -> DtoConverter.getLobbyDetails(this)));
        game.start();
        setState(LobbyState.INGAME);
    }

    /**
     * Allows a player to skip their turn in the game.
     * Only works when the lobby is in the {@link LobbyState#INGAME} state.
     *
     * @param player the player wishing to skip their turn
     */
    public void skip(Player player) {
        synchronized (paramLock) {
            if (disconnectedPlayers.size() == players.size()) return;
        }
        if (state == LobbyState.INGAME) {
            game.skip(player.getShipBoard().orElseThrow());
        }
    }

    @Override
    public void requestRandComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.requestRandComponent(player.getShipBoard().orElseThrow());
    }

    @Override
    public void requestComponent(Player player, int componentID) {
        checkLobbyState(LobbyState.INGAME);
        game.requestComponent(player.getShipBoard().orElseThrow(), componentID);
    }

    @Override
    public void rejectComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.rejectComponent(player.getShipBoard().orElseThrow());
    }

    @Override
    public void stashComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.stashComponent(player.getShipBoard().orElseThrow());
    }

    @Override
    public void grabPlacedComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.grabPlacedComponent(player.getShipBoard().orElseThrow());
    }

    @Override
    public void grabStashedComponent(Player player, int index) {
        checkLobbyState(LobbyState.INGAME);
        game.grabStashedComponent(player.getShipBoard().orElseThrow(), index);
    }

    @Override
    public void placeComponent(Player player, Point point, Direction orientation) {
        checkLobbyState(LobbyState.INGAME);
        game.placeComponent(player.getShipBoard().orElseThrow(), point, orientation);
    }

    @Override
    public void flipHourglass(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.flipHourglass(player.getShipBoard().orElseThrow());
    }

    @Override
    public void placeShipOnFlightBoard(Player player, int startingPosition) {
        checkLobbyState(LobbyState.INGAME);
        game.placeShipOnFlightBoard(player.getShipBoard().orElseThrow(), startingPosition);
    }

    @Override
    public void placeShipOnFlightBoard(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.placeShipOnFlightBoard(player.getShipBoard().orElseThrow());
    }

    @Override
    public void acquireForecast(Player player, int deckIndex) {
        checkLobbyState(LobbyState.INGAME);
        game.acquireForecast(player.getShipBoard().orElseThrow(), deckIndex);
    }

    @Override
    public void releaseForecast(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.releaseForecast(player.getShipBoard().orElseThrow());
    }

    @Override
    public void removeComponent(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        game.removeComponent(player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void chooseShipPiece(Player player, int pieceIndex) {
        checkLobbyState(LobbyState.INGAME);
        game.chooseShipPiece(player.getShipBoard().orElseThrow(), pieceIndex);
    }

    @Override
    public void initializeCabin(Player player, Point point, CrewType crewType) {
        checkLobbyState(LobbyState.INGAME);
        game.initializeCabin(player.getShipBoard().orElseThrow(), point, crewType);
    }

    @Override
    public void drawCard(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.drawCard(player.getShipBoard().orElseThrow());
    }

    @Override
    public void activateComponent(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        game.activateComponent(player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void loseCrew(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        game.loseCrew(player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void grabReward(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.grabReward(player.getShipBoard().orElseThrow());
    }

    @Override
    public void placeGoods(Player player, Point point, GoodsType goodsType) {
        checkLobbyState(LobbyState.INGAME);
        game.placeGoods(player.getShipBoard().orElseThrow(), point, goodsType);
    }

    @Override
    public void removeGoods(Player player, Point point, GoodsType goodsType) {
        checkLobbyState(LobbyState.INGAME);
        game.removeGoods(player.getShipBoard().orElseThrow(), point, goodsType);
    }

    @Override
    public void loseGoods(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        game.loseGood(player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void useBattery(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        game.useBattery(player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void choosePlanet(Player player, int choice) {
        checkLobbyState(LobbyState.INGAME);
        game.choosePlanet(player.getShipBoard().orElseThrow(), choice);
    }

    @Override
    public void goNext(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.goNext(player.getShipBoard().orElseThrow());
    }

    @Override
    public void giveUp(Player player) {
        checkLobbyState(LobbyState.INGAME);
        game.giveUp(player.getShipBoard().orElseThrow());
	}

    @VisibleForTesting
    public void setRemovalDelay(long delay) {
        this.removalDelay = delay;
    }

    @VisibleForTesting
    public void setEventQueue(EventQueue<LobbyEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    @VisibleForTesting
    public void stopEventHandler() {
        this.lobbyEventHandler.stop();
    }
}
