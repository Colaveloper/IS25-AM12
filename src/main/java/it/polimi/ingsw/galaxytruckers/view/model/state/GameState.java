package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract sealed class GameState permits
        AdventureState,
        ShipBuildingState,
        ShipCorrectionState,
        ShipInitializationState
{
    protected Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public abstract List<StateActions> getAvailableActions();

    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRequestComponent(ShipBoard shipBoard, Component componentId) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRejectComponent(ShipBoard shipBoard) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyStashComponent(ShipBoard shipBoard) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyFlipHourglass(ShipBoard shipBoard) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyHourglassEnd() {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        game.getFlightBoard().setShipPosition(shipBoard, position);
    }

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void setForecastDeck(List<AdventureCard> adventureCards) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyReleaseForecast(ShipBoard shipBoard) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyShipValidated(ShipBoard shipBoard) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyDrawCard(AdventureCard adventureCard) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGiveUp(ShipBoard shipBoard) {
        System.err.println("This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }
}