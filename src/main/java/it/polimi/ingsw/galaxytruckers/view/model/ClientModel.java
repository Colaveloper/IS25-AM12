package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.Observer;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClientModel implements ModelObservable {
    
    private final Map<UUID, Lobby> activeLobbies = new HashMap<>();

    private Player clientPlayer = null;
    private ObservableProperty<Game> game = new ObservableProperty<>(null);
    private final Set<Player> players = new HashSet<>();
    private final Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    private final ObservableProperty<MetaState> clientState = new ObservableProperty<>(MetaState.REGISTER);

    private final List<Observer> observers = new ArrayList<>();

    private Map<Player, Integer> finalScores;

    public ClientModel() {

    }

    //region Setup methods

    public void setPlayer(Player player) {
        this.clientPlayer = player;
    }

    public void createGame(Level level, int playersN) {
        game.setValue(new Game(level, playersN));
    }

    public void addPlayer(Player player, GameColor color) {
        players.add(player);
        player.setShipBoard(game.getValue().addShipBoard(color));
        shipToPlayer.put(player.getShipBoard(), player);
    }

    //endregion

    //region Getters
    public Map<UUID, Lobby> getActiveLobbies() {
        return activeLobbies;
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public Game getGame() {
        return game.getValue();
    }

    public ObservableProperty<Game> getGameProperty() {
        return game;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Player getPlayerByShip(ShipBoard shipBoard) {
        return shipToPlayer.get(shipBoard);
    }

    public Map<ShipBoard, Player> getShipToPlayer() {
        return shipToPlayer;
    }

    public Map<Player, Integer> getFinalScores() {
        return finalScores;
    }
    //endregion

    //region Event update methods
    public void notifyCurrentState(GameState gameState) {
        game.getValue().setCurrentState(gameState);
        System.out.println("Updated to state: " + gameState.getClass().getSimpleName());
    }

    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        getGame().getCurrentState().notifyRequestRandComponent(shipBoard, component);
    }

    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        getGame().getCurrentState().notifyRequestComponent(shipBoard, component);
    }

    public void notifyRejectComponent(ShipBoard shipBoard) {
        getGame().getCurrentState().notifyRejectComponent(shipBoard);
    }

    public void notifyStashComponent(ShipBoard shipBoard) {
        getGame().getCurrentState().notifyStashComponent(shipBoard);
    }

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        getGame().getCurrentState().notifyGrabStashedComponent(shipBoard, index);
    }

    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation) {
        getGame().getCurrentState().notifyPlaceComponent(shipBoard, point, orientation);
    }

    public void notifyFlipHourglass(ShipBoard shipBoard) {
        getGame().getCurrentState().notifyFlipHourglass(shipBoard);
    }

    public void notifyHourglassEnd() {
        getGame().getCurrentState().notifyHourglassEnd();
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        getGame().getCurrentState().notifyFlightBoardPosition(shipBoard, position);
    }

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        getGame().getCurrentState().notifyPeekForecast(shipBoard, deckIndex);
    }

    public void setForecastDeck(List<AdventureCard> adventureCards) {
        getGame().getCurrentState().setForecastDeck(adventureCards);
    }

    public void notifyReleaseForecast(ShipBoard shipBoard) {
        getGame().getCurrentState().notifyReleaseForecast(shipBoard);
    }

    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        getGame().getCurrentState().notifyRemoveComponent(shipBoard, point);
    }

    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        getGame().getCurrentState().notifyChooseShipPiece(shipBoard, pieceIndex);
    }

    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        getGame().getCurrentState().notifyShipNotConnected(shipBoard, shipPieces);
    }

    public void notifyShipValidated(ShipBoard shipBoard) {
        getGame().getCurrentState().notifyShipValidated(shipBoard);
    }

    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        getGame().getCurrentState().notifyInitializeCabin(shipBoard, point, crewType);
    }

    public void notifyDrawCard(AdventureCard adventureCard) {
        getGame().getCurrentState().notifyDrawCard(adventureCard);
    }

    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        getGame().getCurrentState().notifyActivateComponent(shipBoard, point);
    }

    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        getGame().getCurrentState().notifyLoseCrew(shipBoard, point);
    }

    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        getGame().getCurrentState().notifyGrabReward(shipBoard, rewardGrabbed);
    }

    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        getGame().getCurrentState().notifyPlaceGoods(shipBoard, point, goodsType);
    }

    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        getGame().getCurrentState().notifyRemoveGoods(shipBoard, point, goodsType);
    }

    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        getGame().getCurrentState().notifyUseBattery(shipBoard,point);
    }

    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        getGame().getCurrentState().notifyChoosePlanet(shipBoard, choice);
    }

    public void notifyGiveUp(ShipBoard shipBoard) {
        getGame().getCurrentState().notifyGiveUp(shipBoard);
    }

    public void setFinalScores(Map<Player, Integer> finalScores) {
        this.finalScores = finalScores;
    }
    //endregion

    public ShipBoard getMyShip() {
        return clientPlayer.getShipBoard();
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.onNotified();
        }
    }

    public ObservableProperty<MetaState> getMetaState() {
        return clientState;
    }

    public void setMetaState(MetaState metaState) {
        this.clientState.setValue(metaState);
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
}
