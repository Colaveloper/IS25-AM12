package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ShipCorrectionState extends GameState {
    Set<ShipBoard> validShipBoards;
    Map<ShipBoard, List<Set<Point>>> shipPieces;

    public ShipCorrectionState() {
        this.validShipBoards = new HashSet<>();
        this.shipPieces = new HashMap<>();
        for (ShipBoard shipBoard : game.getShipBoards()) {
            validateShipBoard(shipBoard);
        }
    }

    private void tryStateTransition() {
        if (validShipBoards.size() == game.getShipBoards().size() && shipPieces.isEmpty()) {
            game.setCurrentState(new ShipInitializationState());
        }
    }

    private void validateShipBoard(ShipBoard shipBoard) {
        if (shipBoard.checkValidity()) {
            validShipBoards.add(shipBoard);
            List<Set<Point>> currentShipPieces = shipBoard.getConnectedSets();
            if (currentShipPieces.size() > 1) {
                shipPieces.put(shipBoard, currentShipPieces);
            }
        }
    }

    @Override
    public void removeComponent(ShipBoard shipBoard, Point point) {
        if (validShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board is already valid");
        }
        shipBoard.removeComponent(point);
        validateShipBoard(shipBoard);
        tryStateTransition();
    }

    @Override
    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        if (!shipPieces.containsKey(shipBoard)) {
            throw new IllegalStateException("You have no ship pieces to choose");
        }
        List<Set<Point>> currentShipPieces = shipPieces.get(shipBoard);
        removeOtherShipPieces(shipBoard, pieceIndex, currentShipPieces);
        shipPieces.remove(shipBoard);
        tryStateTransition();
    }
}
