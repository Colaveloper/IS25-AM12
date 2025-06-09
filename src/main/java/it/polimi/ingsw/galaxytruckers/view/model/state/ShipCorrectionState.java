package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.*;

public final class ShipCorrectionState extends GameState {
    private final Set<ShipBoard> validShipBoards;
    private final Map<ShipBoard, List<Set<Point>>> shipPieces;
    private boolean isConnected;
    private boolean isValid;

    public ShipCorrectionState(ShipBoard myShip, Set<ShipBoard> validShipBoards, Map<ShipBoard, List<Set<Point>>> shipPieces) {
        this.myShip = myShip;
        isValid = validShipBoards.contains(myShip);
        isConnected = !shipPieces.containsKey(myShip);
        this.validShipBoards = validShipBoards;
        this.shipPieces = shipPieces;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        if(!isValid) return List.of(StateActions.REMOVE_COMPONENT);
        if(!isConnected) return List.of(StateActions.CHOOSE_SHIP_PIECE);
        return List.of();
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        shipBoard.removeComponent(point);
        game.getObservers().forEach(observer -> observer.notifyRemoveComponent(shipBoard, point));
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        if(myShip == shipBoard) isConnected = true;
        List<Point> removedPoints = shipBoard.removeShipPiece(shipPieces.get(shipBoard), pieceIndex);
        game.getObservers().forEach(observer -> observer.notifyChooseShipPiece(shipBoard, pieceIndex, removedPoints));
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        if(myShip == shipBoard) {
            isConnected = false;
            isValid = true;
        }
        validShipBoards.add(shipBoard);
        this.shipPieces.put(shipBoard, shipPieces);
        game.getObservers().forEach(observer -> observer.notifyShipNotConnected(shipBoard, shipPieces));
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        if(myShip == shipBoard) {
            isConnected = true;
            isValid = true;
        }
        shipPieces.remove(shipBoard);
        game.getObservers().forEach(observer -> observer.notifyShipValidated(shipBoard));
    }

    public Set<ShipBoard> getValidShipBoards() {
        return validShipBoards;
    }

    public Map<ShipBoard, List<Set<Point>>> getShipPieces() {
        return shipPieces;
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        int numResidents = shipBoard.initializeCabin(point, crewType);
        game.getObservers().forEach(observer -> observer.notifyInitializeCabin(shipBoard, point, crewType, numResidents));
    }

    //TODO: notify ship is valid, notify shipPieces
}
