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

    public Player getHost() {
        return host;
    }

    public UUID getId() {
        return id;
    }

    public Level getLevel() {
        return level;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public List<Player> getPlayers() {
        List<Player> res;
        synchronized (paramLock) {
            res = new ArrayList<>(players);
        }
        return res;
    }

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

    public void notifyPlayerExit(Player player) {
        eventQueue.notifyEvent(new PlayerExitEvent(player.getNickname()));
    }

    public void remove() {
        this.removeLobby.accept(this);
        this.lobbyEventHandler.stop();
    }

    private void checkLobbyState(LobbyState lobbyState) {
        if (state != lobbyState) {
            throw new IllegalStateException("The lobby is not in " + lobbyState.toString());
        }
    }

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
