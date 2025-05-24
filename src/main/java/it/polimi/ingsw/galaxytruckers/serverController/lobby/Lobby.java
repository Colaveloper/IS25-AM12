package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Lobby implements LobbyInterface {
    private final GameModelInterface model;
    private final ServerController controller;

    private final UUID id;
    private final Level level;
    private final int numPlayers;

    private final AtomicReference<LobbyState> state;
    private final AtomicReference<Game> game;
    private final List<Player> players;
    private final Set<FourColors> chosenColors;
    private final Map<String, FourColors> playerColors;

    private final EventQueue eventQueue;
    private final EventQueueHandler eventQueueHandler;

    public Lobby(GameModelInterface model, ServerController serverController, Player creator, Level level, int numPlayers) {
        this.model = model;
        this.controller = serverController;
        this.id = UUID.randomUUID();
        this.level = level;
        this.numPlayers = numPlayers;

        this.state = new AtomicReference<>(LobbyState.PREPARATION);
        this.game = new AtomicReference<>(null);
        this.players = new ArrayList<>();
        this.chosenColors = new HashSet<>();
        this.playerColors = new HashMap<>();

        this.eventQueue = new EventQueue();
        this.eventQueueHandler = new EventQueueHandler(this);

        eventQueueHandler.start();
        addPlayer(creator);
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
        synchronized (players) {
            return new ArrayList<>(players);
        }
    }

    public LobbyState getState() {
        return state.get();
    }

    public Optional<Game> getGame() {
        return Optional.ofNullable(game.get());
    }

    public Set<FourColors> getChosenColors() {
        synchronized (chosenColors) {
            return chosenColors;
        }
    }

    public EventQueue getEventQueue() {
        return eventQueue;
    }

    public EventQueueHandler getEventQueueHandler() {
        return eventQueueHandler;
    }

    public Map<String, FourColors> getPlayerColors() {
        synchronized (playerColors) {
            return new HashMap<>(playerColors);
        }
    }

    public void addPlayer(Player player) {
        checkLobbyState(LobbyState.PREPARATION);
        FourColors chosenColor;
        synchronized (chosenColors) {
            chosenColor = Arrays.stream(FourColors.values())
                    .filter(c -> !chosenColors.contains(c))
                    .findAny().orElseThrow();
            chosenColors.add(chosenColor);
        }
        player.setColor(chosenColor);
        synchronized (players) {
            players.add(player);
        }
        synchronized (playerColors) {
            playerColors.put(player.getNickname(), chosenColor);
        }
        player.setLobby(this);
        eventQueue.notifyEvent(new JoinLobbyEvent(player.getNickname()));
        if (players.size() == numPlayers) {
            startGame();
        }
    }

    public void notifyPlayerDisconnection(Player player) {
        eventQueue.notifyEvent(new PlayerDisconnectionEvent(player.getNickname()));
    }

    public void notifyPlayerExit(Player player) {

    }

    private void checkLobbyState(LobbyState lobbyState) {
        if (state.get() != lobbyState) {
            throw new IllegalStateException("The lobby is not in " + lobbyState.toString());
        }
    }

    public void setState(LobbyState state) {
        this.state.set(state);
    }

    public void setGame(Game game) {
        this.game.set(game);
    }

    private void startGame() {
        Game game = model.createGame(this.level);
        for (Player player : getPlayers()) {
            ShipBoard ship = model.addShip(game, player.getColor().orElseThrow(
                    () -> new IllegalStateException("Player " + player.getNickname() + " has not chosen a color")
            ));
            player.setShipBoard(ship);
        }
        setState(LobbyState.INGAME);
        model.setEventListener(game,eventQueue);
        model.startGame(game);
        setGame(game);
    }

    @Override
    public void requestRandComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.requestRandComponent(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void requestComponent(Player player, int componentID) {
        checkLobbyState(LobbyState.INGAME);
        model.requestComponent(game.get(),player.getShipBoard().orElseThrow(),componentID);
    }

    @Override
    public void rejectComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.rejectComponent(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void stashComponent(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.stashComponent(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void grabStashedComponent(Player player, int index) {
        checkLobbyState(LobbyState.INGAME);
        model.grabStashedComponent(game.get(),player.getShipBoard().orElseThrow(),index);
    }

    @Override
    public void placeComponent(Player player, Point point, int orientation) {
        checkLobbyState(LobbyState.INGAME);
        model.placeComponent(game.get(),player.getShipBoard().orElseThrow(),point,orientation);
    }

    @Override
    public void flipHourglass(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.flipHourglass(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void placeShipOnFlightBoard(Player player, int startingPosition) {
        checkLobbyState(LobbyState.INGAME);
        model.placeShipOnFlightBoard(game.get(),player.getShipBoard().orElseThrow(),startingPosition);
    }

    @Override
    public void acquireForecast(Player player, int deckIndex) {
        checkLobbyState(LobbyState.INGAME);
        model.acquireForecast(game.get(),player.getShipBoard().orElseThrow(),deckIndex);
    }

    @Override
    public void releaseForecast(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.releaseForecast(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void removeComponent(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.removeComponent(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void chooseShipPiece(Player player, int pieceIndex) {
        checkLobbyState(LobbyState.INGAME);
        model.chooseShipPiece(game.get(),player.getShipBoard().orElseThrow(),pieceIndex);
    }

    @Override
    public void initializeCabin(Player player, Point point, CrewType crewType) {
        checkLobbyState(LobbyState.INGAME);
        model.initializeCabin(game.get(),player.getShipBoard().orElseThrow(),point,crewType);
    }

    @Override
    public void drawCard(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.drawCard(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void activateComponent(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.activateComponent(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void loseCrew(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.loseCrew(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void grabReward(Player player, boolean rewardGrabbed) {
        checkLobbyState(LobbyState.INGAME);
        model.grabReward(game.get(),player.getShipBoard().orElseThrow(),rewardGrabbed);
    }

    @Override
    public void placeGoods(Player player, Point point, GoodsType goodsType) {
        checkLobbyState(LobbyState.INGAME);
        model.placeGoods(game.get(),player.getShipBoard().orElseThrow(),point,goodsType);
    }

    @Override
    public void removeGoods(Player player, Point point, GoodsType goodsType) {
        checkLobbyState(LobbyState.INGAME);
        model.removeGoods(game.get(),player.getShipBoard().orElseThrow(),point,goodsType);
    }

    @Override
    public void loseGoods(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.loseGood(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void useBattery(Player player, Point point) {
        checkLobbyState(LobbyState.INGAME);
        model.useBattery(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void choosePlanet(Player player, int choice) {
        checkLobbyState(LobbyState.INGAME);
        model.choosePlanet(game.get(),player.getShipBoard().orElseThrow(),choice);
    }

    @Override
    public void goNext(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.goNext(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void giveUp(Player player) {
        checkLobbyState(LobbyState.INGAME);
        model.giveUp(game.get(), player.getShipBoard().orElseThrow());
    }
}
