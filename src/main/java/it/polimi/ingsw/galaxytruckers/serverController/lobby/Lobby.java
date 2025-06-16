package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Lobby implements LobbyInterface {
    private final Object preparationLock = new Object();

    private final GameModelInterface model;
    private final UUID id;
    private final Level level;
    private final int numPlayers;
    private final Player host;

    private volatile LobbyState state;
    private volatile Game game;
    private final List<Player> players;
    private final Set<GameColor> chosenColors;
    private final Map<Player, GameColor> playerColors;

    private final EventQueue<LobbyEvent> eventQueue;
    private final LobbyEventHandler lobbyEventHandler;

    private final Consumer<Lobby> removeLobby;

    public Lobby(GameModelInterface model, Player creator, Level level, int numPlayers, Consumer<Lobby> removeLobby) {
        this.model = model;
        this.id = UUID.randomUUID();
        this.level = level;
        this.numPlayers = numPlayers;
        this.host = creator;
        this.removeLobby = removeLobby;

        this.state = LobbyState.PREPARATION;
        this.game = null;
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
        synchronized (preparationLock) {
            return new ArrayList<>(players);
        }
    }

    /**
     * Adds a player to the lobby, assigning a color among the remaining ones.
     * If {@link #numPlayers} is not reached, enqueues the joining event,
     * otherwise calls {@link #startGame()}
     *
     * @param player the player to add
     */
    public boolean addPlayer(Player player) {
        synchronized (preparationLock) {
            checkLobbyState(LobbyState.PREPARATION);
            GameColor chosenColor = Arrays.stream(GameColor.values())
                    .filter(c -> !chosenColors.contains(c))
                    .findAny().orElseThrow();
            chosenColors.add(chosenColor);
            players.add(player);
            playerColors.put(player, chosenColor);
            player.setLobby(this);
            eventQueue.notifyEvent(new LobbyDetailsEvent(
                    player.getNickname(),
                    this.getId(),
                    playerColors.entrySet().stream()
                            .collect(Collectors.toMap(
                                    e -> e.getKey().getNickname(),
                                    Map.Entry::getValue
                            )),
                    level,
                    numPlayers
            ));
            eventQueue.notifyEvent(new JoinLobbyEvent(player.getNickname(), playerColors.get(player)));
            if (players.size() == numPlayers) {
                startGame();
                return true;
            }
            return false;
        }
    }

    public void notifyPlayerDisconnection(Player player) {
        eventQueue.notifyEvent(new PlayerDisconnectionEvent(player.getNickname()));
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

    /**
     * Brings the state of the lobby to {@link LobbyState#INGAME}
     * Sets the game of the lobby to a new instance of {@link Game} of the specified level
     */
    private void startGame() {
        Game game = model.createGame(this.level, this.numPlayers);
        for (Player player : getPlayers()) {
            ShipBoard ship = model.addShip(game, playerColors.get(player));
            player.setShipBoard(ship);
        }
        model.setEventListener(game, eventQueue);
        model.startGame(game);
        this.game = game;
        setState(LobbyState.INGAME);
    }

    @Override
    public void requestRandComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.requestRandComponent(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void requestComponent(Player player, int componentID) {
        checkLobbyState(LobbyState.INGAME);
        model.requestComponent(game, player.getShipBoard().orElseThrow(), componentID);
    }

    @Override
    public void rejectComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.rejectComponent(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void stashComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.stashComponent(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void grabPlacedComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.grabPlacedComponent(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void grabStashedComponent(Player player, int index) {
        checkLobbyState(LobbyState.INGAME);
        model.grabStashedComponent(game, player.getShipBoard().orElseThrow(), index);
    }

    @Override
    public void placeComponent(Player player, Point point, Direction orientation) {
        checkLobbyState(LobbyState.INGAME);
        model.placeComponent(game, player.getShipBoard().orElseThrow(), point, orientation);
    }

    @Override
    public void flipHourglass(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.flipHourglass(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void placeShipOnFlightBoard(Player player, int startingPosition) {
        checkLobbyState(LobbyState.INGAME);
        model.placeShipOnFlightBoard(game, player.getShipBoard().orElseThrow(), startingPosition);
    }

    @Override
    public void acquireForecast(Player player, int deckIndex) {
        checkLobbyState(LobbyState.INGAME);
        model.acquireForecast(game, player.getShipBoard().orElseThrow(), deckIndex);
    }

    @Override
    public void releaseForecast(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.releaseForecast(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void removeComponent(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.removeComponent(game, player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void chooseShipPiece(Player player, int pieceIndex) {
        checkLobbyState(LobbyState.INGAME);
        model.chooseShipPiece(game, player.getShipBoard().orElseThrow(), pieceIndex);
    }

    @Override
    public void initializeCabin(Player player, Point point, CrewType crewType) {
        checkLobbyState(LobbyState.INGAME);
        model.initializeCabin(game, player.getShipBoard().orElseThrow(), point, crewType);
    }

    @Override
    public void drawCard(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.drawCard(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void activateComponent(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.activateComponent(game, player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void loseCrew(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.loseCrew(game, player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void grabReward(Player player, boolean rewardGrabbed) {
        checkLobbyState(LobbyState.INGAME);
        model.grabReward(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void placeGoods(Player player, Point point, GoodsType goodsType) {
        checkLobbyState(LobbyState.INGAME);
        model.placeGoods(game, player.getShipBoard().orElseThrow(), point, goodsType);
    }

    @Override
    public void removeGoods(Player player, Point point, GoodsType goodsType) {
        checkLobbyState(LobbyState.INGAME);
        model.removeGoods(game, player.getShipBoard().orElseThrow(), point, goodsType);
    }

    @Override
    public void loseGoods(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.loseGood(game, player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void useBattery(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.useBattery(game, player.getShipBoard().orElseThrow(), point);
    }

    @Override
    public void choosePlanet(Player player, int choice) {
        checkLobbyState(LobbyState.INGAME);
        model.choosePlanet(game, player.getShipBoard().orElseThrow(), choice);
    }

    @Override
    public void goNext(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.goNext(game, player.getShipBoard().orElseThrow());
    }

    @Override
    public void giveUp(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.giveUp(game, player.getShipBoard().orElseThrow());
	}
}
