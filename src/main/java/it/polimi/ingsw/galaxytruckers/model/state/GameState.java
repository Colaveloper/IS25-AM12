package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public abstract class GameState {
    protected Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    static void removeOtherShipPieces(ShipBoard shipBoard, int pieceIndex, List<Set<Point>> currentShipPieces) {
        if (pieceIndex < 0 || pieceIndex >= currentShipPieces.size()) {
            throw new IllegalArgumentException("Invalid piece index");
        }
        List<Point> componentsToRemove = IntStream.range(0, currentShipPieces.size())
                .filter(x -> x != pieceIndex)
                .mapToObj(currentShipPieces::get)
                .flatMap(Collection::stream)
                .toList();
        for (Point p : componentsToRemove) {
            shipBoard.removeComponent(p);
        }
    }

    public void activateComponent(ShipBoard shipBoard, Point position) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void spendBatteries(ShipBoard shipBoard, Point point, int amount) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void grabReward(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void drawCard(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void loseCrew(ShipBoard shipBoard, Point position)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void loseGood(ShipBoard shipBoard, Point position)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void addGood(ShipBoard shipBoard, Point position, GoodsType good)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void removeGood(ShipBoard shipBoard, Point position, GoodsType good)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void goNext(ShipBoard shipBoard) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void choosePlanet(ShipBoard shipBoard, int option) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void requestRandComponent(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void requestComponent(ShipBoard shipBoard, int componentId){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void rejectComponent(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void stashComponent(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void grabStashedComponent(ShipBoard shipBoard, int index){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void placeComponent(ShipBoard shipBoard, Point point, int orientation){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void flipHourglass(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void acquireForecast(ShipBoard shipBoard, int deckIndex){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void releaseForecast(ShipBoard shipBoard){
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void removeComponent(ShipBoard shipBoard, Point point) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void initializeCabin(ShipBoard shipBoard, CrewType crewType) {
        throw new IllegalStateException("This action is unsupported in this state");
    }
}