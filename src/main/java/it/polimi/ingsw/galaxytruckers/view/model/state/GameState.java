package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;

public abstract class GameState {
    protected Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public abstract List<StateActions> getAvailableActions();

    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRequestComponent(ShipBoard shipBoard, Component componentId) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRejectComponent(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyStashComponent(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyFlipHourglass(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyHourglassEnd() {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        game.getFlightBoard().setShipPosition(shipBoard, position);
    }

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void setForecastDeck(List<AdventureCard> adventureCards) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyReleaseForecast(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyDrawCard(ShipBoard shipBoard, AdventureCard adventureCard) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGiveUp(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }
}