package it.polimi.ingsw.galaxytruckers.model;
// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventListener;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.LobbyEvent;

import java.awt.*;

public class GameModel implements GameModelInterface {
    @Override
    public Game createGame(Level level, int shipsN) {
        return new Game(level, shipsN);
    }

    @Override
    public ShipBoard addShip(Game game, GameColor color) {
        return game.addShipBoard(color);
    }

    @Override
    public void setEventListener(Game game, EventListener<LobbyEvent> listener) {
        GameEventListener gameEventListener = new GameEventListener();
        gameEventListener.setControllerListener(listener);
        game.setEventListener(gameEventListener);
    }

    @Override
    public void startGame(Game game) {
        game.start();
    }

    @Override
    public void requestRandComponent(Game game, ShipBoard shipBoard) {
        game.requestRandComponent(shipBoard);
    }

    @Override
    public void requestComponent(Game game, ShipBoard shipBoard, int componentID) {
        game.requestComponent(shipBoard, componentID);
    }

    @Override
    public void rejectComponent(Game game, ShipBoard shipBoard) {
        game.rejectComponent(shipBoard);
    }

    @Override
    public void stashComponent(Game game, ShipBoard shipBoard) {
        game.stashComponent(shipBoard);
    }

    @Override
    public void grabPlacedComponent(Game game, ShipBoard shipBoard) {
        game.grabPlacedComponent(shipBoard);
    }

    @Override
    public void grabStashedComponent(Game game, ShipBoard shipBoard, int index) {
        game.grabStashedComponent(shipBoard, index);
    }

    @Override
    public void placeComponent(Game game, ShipBoard shipBoard, Point point, Direction orientation) {
        game.placeComponent(shipBoard, point, orientation);
    }

    @Override
    public void flipHourglass(Game game, ShipBoard shipBoard) {
        game.flipHourglass(shipBoard);
    }

    @Override
    public void placeShipOnFlightBoard(Game game, ShipBoard shipBoard, int startingPosition) {
        game.placeShipOnFlightBoard(shipBoard, startingPosition);
    }

    @Override
    public void placeShipOnFlightBoard(Game game, ShipBoard shipBoard) {
        game.placeShipOnFlightBoard(shipBoard);
    }

    @Override
    public void acquireForecast(Game game, ShipBoard shipBoard, int deckIndex) {
        game.acquireForecast(shipBoard, deckIndex);
    }

    @Override
    public void releaseForecast(Game game, ShipBoard shipBoard) {
        game.releaseForecast(shipBoard);
    }

    @Override
    public void removeComponent(Game game, ShipBoard shipBoard, Point point) {
        game.removeComponent(shipBoard, point);
    }

    @Override
    public void chooseShipPiece(Game game, ShipBoard shipBoard, int pieceIndex) {
        game.chooseShipPiece(shipBoard, pieceIndex);
    }

    @Override
    public void initializeCabin(Game game, ShipBoard shipBoard, Point point, CrewType crewType) {
        game.initializeCabin(shipBoard, point, crewType);
    }

    @Override
    public void activateComponent(Game game, ShipBoard shipBoard, Point point) {
        game.activateComponent(shipBoard, point);
    }

    @Override
    public void loseCrew(Game game, ShipBoard shipBoard, Point point) {
        game.loseCrew(shipBoard, point);
    }

    @Override
    public void grabReward(Game game, ShipBoard shipBoard) {
        game.grabReward(shipBoard);
    }

    @Override
    public void placeGoods(Game game, ShipBoard shipBoard, Point point, GoodsType goodsType) {
        game.placeGoods(shipBoard, point, goodsType);
    }

    @Override
    public void removeGoods(Game game, ShipBoard shipBoard, Point point, GoodsType goodsType) {
        game.removeGoods(shipBoard, point, goodsType);
    }

    @Override
    public void useBattery(Game game, ShipBoard shipBoard, Point point) {
        game.useBattery(shipBoard,point);
    }

    @Override
    public void choosePlanet(Game game, ShipBoard shipBoard, int choice) {
        game.choosePlanet(shipBoard, choice);
    }

    @Override
    public void giveUp(Game game, ShipBoard shipBoard) {
        game.giveUp(shipBoard);
    }

    @Override
    public void drawCard(Game game, ShipBoard shipBoard) {
        game.drawCard(shipBoard);
    }

    @Override
    public void loseGood(Game game, ShipBoard shipBoard, Point point) {
        game.loseGood(shipBoard, point);
    }

    @Override
    public void goNext(Game game, ShipBoard shipBoard) {
        game.goNext(shipBoard);
    }
}
