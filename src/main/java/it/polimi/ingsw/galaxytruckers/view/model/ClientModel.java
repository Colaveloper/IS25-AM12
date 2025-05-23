package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClientModel {

    private final Map<UUID, Lobby> activeLobbies = new HashMap<>();

    private Player clientPlayer = null;
    private Game game = null;
    private final Set<Player> players = new HashSet<>();
    private final Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    private Map<Player, Integer> finalScores;

    //region Setup methods

    public void setPlayer(Player player) {
        this.clientPlayer = player;
    }

    public void createGame(Level level, int playersN) {
        game = new Game(level, playersN);
    }

    public void addPlayer(Player player, FourColors color) {
        players.add(player);
        player.setShipBoard(game.addShipBoard(color));
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
        return game;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Player getPlayerByShip(ShipBoard shipBoard) {
        return shipToPlayer.get(shipBoard);
    }

    public Map<Player, Integer> getFinalScores() {
        return finalScores;
    }
    //endregion

    //region Event update methods
    public void notifyCurrentState(GameState gameState) {
        game.setCurrentState(gameState);
    }

    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        game.getCurrentState().notifyRequestRandComponent(shipBoard, component);
    }

    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        game.getCurrentState().notifyRequestComponent(shipBoard, component);
    }

    public void notifyRejectComponent(ShipBoard shipBoard) {
        game.getCurrentState().notifyRejectComponent(shipBoard);
    }

    public void notifyStashComponent(ShipBoard shipBoard) {
        game.getCurrentState().notifyStashComponent(shipBoard);
    }

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        game.getCurrentState().notifyGrabStashedComponent(shipBoard, index);
    }

    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation) {
        game.getCurrentState().notifyPlaceComponent(shipBoard, point, orientation);
    }

    public void notifyFlipHourglass(ShipBoard shipBoard) {
        game.getCurrentState().notifyFlipHourglass(shipBoard);
    }

    public void notifyHourglassEnd() {
        game.getCurrentState().notifyHourglassEnd();
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        game.getCurrentState().notifyFlightBoardPosition(shipBoard, position);
    }

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        game.getCurrentState().notifyPeekForecast(shipBoard, deckIndex);
    }

    public void setForecastDeck(List<AdventureCard> adventureCards) {
        game.getCurrentState().setForecastDeck(adventureCards);
    }

    public void notifyReleaseForecast(ShipBoard shipBoard) {
        game.getCurrentState().notifyReleaseForecast(shipBoard);
    }

    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        game.getCurrentState().notifyRemoveComponent(shipBoard, point);
    }

    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        game.getCurrentState().notifyChooseShipPiece(shipBoard, pieceIndex);
    }

    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        game.getCurrentState().notifyInitializeCabin(shipBoard, point, crewType);
    }

    public void notifyDrawCard(ShipBoard shipBoard, AdventureCard adventureCard) {
        game.getCurrentState().notifyDrawCard(shipBoard, adventureCard);
    }

    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        game.getCurrentState().notifyActivateComponent(shipBoard, point);
    }

    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        game.getCurrentState().notifyLoseCrew(shipBoard, point);
    }

    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        game.getCurrentState().notifyGrabReward(shipBoard, rewardGrabbed);
    }

    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        game.getCurrentState().notifyPlaceGoods(shipBoard, point, goodsType);
    }

    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        game.getCurrentState().notifyRemoveGoods(shipBoard, point, goodsType);
    }

    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        game.getCurrentState().notifyUseBattery(shipBoard,point);
    }

    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        game.getCurrentState().notifyChoosePlanet(shipBoard, choice);
    }

    public void notifyGiveUp(ShipBoard shipBoard) {
        game.getCurrentState().notifyGiveUp(shipBoard);
    }

    public void setFinalScores(Map<Player, Integer> finalScores) {
        this.finalScores = finalScores;
    }
    //endregion
}
