package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
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

    protected ShipBoard myShip;

    public abstract List<StateActions> getAvailableActions();

    public void leave() {
        game.getShipBoards().forEach(ShipBoard::deactivateAll);
    }

    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        System.err.println("1This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRequestComponent(ShipBoard shipBoard, Component componentId) {
        System.err.println("2This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRejectComponent(ShipBoard shipBoard) {
        System.err.println("3This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyStashComponent(ShipBoard shipBoard) {
        System.err.println("4This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        System.err.println("5This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        System.err.println("6This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyFlipHourglass(ShipBoard shipBoard) {
        System.err.println("7This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyHourglassEnd() {
        System.err.println("8This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position, boolean isMyShip) {
        game.getFlightBoard().setShipPosition(shipBoard, position);
    }

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        System.err.println("9This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void setForecastDeck(List<AdventureCard> adventureCards) {
        System.err.println("10This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyReleaseForecast(ShipBoard shipBoard, boolean isMyShip) {
        System.err.println("11This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        System.err.println("12This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        System.err.println("13This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        System.err.println("14This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyShipValidated(ShipBoard shipBoard) {
        System.err.println("15This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        System.err.println("16This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyDrawCard(AdventureCard adventureCard) {
        System.err.println("17This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        System.err.println("18This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        System.err.println("19This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGrabReward(ShipBoard shipBoard, int credits) {
        System.err.println("20This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        System.err.println("21This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        System.err.println("22This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        System.err.println("23This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipboard) {
        System.err.println("24This action is not permitted in this state, \n" +
                "it seems the client and the server are out of sync");
    }

    public void notifyGiveUp(ShipBoard shipBoard) {
        System.err.println(shipBoard.getColor() + " is giving up");
    }
}