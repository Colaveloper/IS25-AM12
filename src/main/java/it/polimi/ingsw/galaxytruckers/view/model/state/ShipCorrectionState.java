package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.*;

public final class ShipCorrectionState extends GameState {
    Set<ShipBoard> validShipBoards;
    Map<ShipBoard, List<Set<Point>>> shipPieces;

    public ShipCorrectionState() {
        this.validShipBoards = new HashSet<>();
        this.shipPieces = new HashMap<>();
    }

    @Override
    public List<StateActions> getAvailableActions() {
        //TODO: logic for retrieving the client's shipboard and checking if
        // they should remove or choose ship piece
        return List.of(
                StateActions.REMOVE_COMPONENT,
                StateActions.CHOOSE_SHIP_PIECE
        );
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        shipBoard.removeComponent(point);
        game.getObservers().forEach(observer -> observer.notifyRemoveComponent(shipBoard, point));
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        List<Point> removedPoints = shipBoard.removeShipPiece(shipPieces.get(shipBoard), pieceIndex);
        game.getObservers().forEach(observer -> observer.notifyChooseShipPiece(shipBoard, pieceIndex, removedPoints));
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        validShipBoards.add(shipBoard);
        this.shipPieces.put(shipBoard, shipPieces);
        game.getObservers().forEach(observer -> observer.notifyShipNotConnected(shipBoard, shipPieces));
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        shipPieces.remove(shipBoard);
        game.getObservers().forEach(observer -> observer.notifyShipValidated(shipBoard));
    }

    public Set<ShipBoard> getValidShipBoards() {
        return validShipBoards;
    }

    public Map<ShipBoard, List<Set<Point>>> getShipPieces() {
        return shipPieces;
    }

    //TODO: notify ship is valid, notify shipPieces
}
