package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueueHandler;
import it.polimi.ingsw.galaxytruckers.serverController.events.StartBuildingEvent;
import org.checkerframework.checker.units.qual.A;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Lobby implements LobbyInterface {
    private final GameModelInterface model;
    private final UUID id;
    private final Level level;
    private final int numPlayers;

    private final AtomicReference<LobbyState> state;
    private final AtomicReference<Game> game;
    private final List<Player> players;
    private final Set<FourColors> chosenColors;

    private final EventQueue eventQueue;
    private final EventQueueHandler eventQueueHandler;

    public Lobby(GameModelInterface model, Player creator, Level level, int numPlayers) {
        this.model = model;
        this.id = UUID.randomUUID();
        this.level = level;
        this.numPlayers = numPlayers;

        this.state = new AtomicReference<>(LobbyState.PREPARATION);
        this.game = new AtomicReference<>(null);
        this.players = Collections.synchronizedList(new ArrayList<>());
        this.players.add(creator);
        this.chosenColors = Collections.synchronizedSet(new HashSet<>());

        this.eventQueue = new EventQueue();
        this.eventQueueHandler = new EventQueueHandler(this);
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
        return players;
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

    public synchronized void addPlayer(Player player) {
        if (this.state.get() != LobbyState.PREPARATION) {
            throw new IllegalStateException("The lobby is not in preparation");
        }
        FourColors chosenColor = Arrays.stream(FourColors.values())
                        .filter(c -> !chosenColors.contains(c))
                                .findAny().orElseThrow();
        player.setColor(chosenColor);
        players.add(player);
        player.setLobby(this);
        if (players.size() == numPlayers) {
            startGame();
        }
    }

    public void setState(LobbyState state) {
        this.state.set(state);
    }

    public void setGame(Game game) {
        this.game.set(game);
    }

    private synchronized void startGame() {
        Game game = model.createGame(this.level);
        for (Player player : players) {
            ShipBoard ship = model.addShip(game, player.getColor().orElseThrow(
                    () -> new IllegalStateException("Player " + player.getNickname() + " has not chosen a color")
            ));
            player.setShipBoard(ship);
        }
        setState(LobbyState.INGAME);
        eventQueue.notifyEvent(new StartBuildingEvent());
        setGame(game);
    }

    @Override
    public void requestRandComponent(Player player) {
        model.requestRandComponent(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void requestComponent(Player player, int componentID) {
        model.requestComponent(game.get(),player.getShipBoard().orElseThrow(),componentID);
    }

    @Override
    public void rejectComponent(Player player) {
        model.rejectComponent(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void stashComponent(Player player) {
        model.stashComponent(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void grabStashedComponent(Player player, int index) {
        model.grabStashedComponent(game.get(),player.getShipBoard().orElseThrow(),index);
    }

    @Override
    public void placeComponent(Player player, Point point, int orientation) {
        model.placeComponent(game.get(),player.getShipBoard().orElseThrow(),point,orientation);
    }

    @Override
    public void flipHourglass(Player player) {
        model.flipHourglass(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void placeShipOnFlightBoard(Player player, int startingPosition) {
        model.placeShipOnFlightBoard(game.get(),player.getShipBoard().orElseThrow(),startingPosition);
    }

    @Override
    public void acquireForecast(Player player, int deckIndex) {
        model.acquireForecast(game.get(),player.getShipBoard().orElseThrow(),deckIndex);
    }

    @Override
    public void releaseForecast(Player player) {
        model.releaseForecast(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void removeComponent(Player player, Point point) {
        model.removeComponent(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void chooseShipPiece(Player player, int pieceIndex) {
        model.chooseShipPiece(game.get(),player.getShipBoard().orElseThrow(),pieceIndex);
    }

    @Override
    public void initializeCabin(Player player, Point point, CrewType crewType) {
        model.initializeCabin(game.get(),player.getShipBoard().orElseThrow(),point,crewType);
    }

    @Override
    public void drawCard(Player player) {
        model.drawCard(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void activateComponent(Player player, Point point) {
        model.activateComponent(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void loseCrew(Player player, Point point) {
        model.loseCrew(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void grabReward(Player player, boolean rewardGrabbed) {
        model.grabReward(game.get(),player.getShipBoard().orElseThrow(),rewardGrabbed);
    }

    @Override
    public void placeGoods(Player player, Point point, GoodsType goodsType) {
        model.placeGoods(game.get(),player.getShipBoard().orElseThrow(),point,goodsType);
    }

    @Override
    public void removeGoods(Player player, Point point, GoodsType goodsType) {
        model.removeGoods(game.get(),player.getShipBoard().orElseThrow(),point,goodsType);
    }

    @Override
    public void loseGoods(Player player, Point point) {
        model.loseGood(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void useBattery(Player player, Point point) {
        model.useBattery(game.get(),player.getShipBoard().orElseThrow(),point);
    }

    @Override
    public void choosePlanet(Player player, int choice) {
        model.choosePlanet(game.get(),player.getShipBoard().orElseThrow(),choice);
    }

    @Override
    public void goNext(Player player) {
        model.goNext(game.get(),player.getShipBoard().orElseThrow());
    }

    @Override
    public void giveUp(Player player) {
        model.giveUp(game.get(),player.getShipBoard().orElseThrow());
    }
}
