package it.polimi.ingsw.galaxytruckers.model;
// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.io.IOException;
import java.util.List;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameModel implements GameModelInterface {
    @Override
    public Game createGame(Level level) {
        return new Game(level);
    }

    @Override
    public ShipBoard addShip(Game game, Colors color) {
        return game.addShipBoard(color);
    }

    @Override
    public void requestRandComponent(Game game, ShipBoard shipBoard) {
        game.getCurrentState().requestRandComponent(shipBoard);
    }

    @Override
    public void requestComponent(Game game, ShipBoard shipBoard, int componentID) {
        game.getCurrentState().requestComponent(shipBoard, componentID);
    }

    @Override
    public void rejectComponent(Game game, ShipBoard shipBoard) {
        game.getCurrentState().rejectComponent(shipBoard);
    }

    @Override
    public void stashComponent(Game game, ShipBoard shipBoard) {
        game.getCurrentState().stashComponent(shipBoard);
    }

    @Override
    public void grabStashedComponent(Game game, ShipBoard shipBoard, int index) {
        game.getCurrentState().grabStashedComponent(shipBoard, index);
    }

    @Override
    public void placeComponent(Game game, ShipBoard shipBoard, Point point, int orientation) {
        game.getCurrentState().placeComponent(shipBoard, point, orientation);
    }

    @Override
    public void flipHourglass(Game game, ShipBoard shipBoard) {
        game.getCurrentState().flipHourglass(shipBoard);
    }

    @Override
    public void placeShipOnFlightBoard(Game game, ShipBoard shipBoard, int startingPosition) {
        game.getCurrentState().placeShipOnFlightBoard(shipBoard, startingPosition);
    }

    @Override
    public void acquireForecast(Game game, ShipBoard shipBoard, int deckIndex) {
        game.getCurrentState().acquireForecast(shipBoard, deckIndex);
    }

    @Override
    public void releaseForecast(Game game, ShipBoard shipBoard) {
        game.getCurrentState().releaseForecast(shipBoard);
    }

    @Override
    public void removeComponent(Game game, ShipBoard shipBoard, Point point) {
        game.getCurrentState().removeComponent(shipBoard, point);
    }

    @Override
    public void chooseShipPiece(Game game, ShipBoard shipBoard, int pieceIndex) {
        game.getCurrentState().chooseShipPiece(shipBoard, pieceIndex);
    }

    @Override
    public void initializeCabin(CrewType crewType) {
        //TODO: create or edit a state to implement this behavior
    }

    @Override
    public void activateComponent(Game game, ShipBoard shipBoard, Point point) {
        game.getCurrentState().activateComponent(shipBoard, point);
    }

    @Override
    public void loseCrew(Game game, ShipBoard shipBoard, Point point) {
        game.getCurrentState().loseCrew(shipBoard, point);
    }

    @Override
    public void grabReward(Game game, ShipBoard shipBoard, boolean rewardGrabbed) {
        game.getCurrentState().grabReward(shipBoard);
    }

    @Override
    public void placeGoods(Game game, ShipBoard shipBoard, Point point, GoodsType goodsType) {
        game.getCurrentState().addGood(shipBoard, point, goodsType);
    }

    @Override
    public void removeGoods(Game game, ShipBoard shipBoard, Point point, GoodsType goodsType) {
        game.getCurrentState().removeGood(shipBoard, point, goodsType);
    }

    @Override
    public void useBattery(Game game, ShipBoard shipBoard, Point point) {
        game.getCurrentState().spendBatteries(shipBoard,point,1);
    }

    @Override
    public void giveUp(Game game, ShipBoard shipBoard) {
        //TODO: add give up method in AdventureStates
    }
}
