package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;


public final class ChooseShipPieceState extends AdventureState implements GameStateInterface {
    private final ShipBoard shipBoard;
    private final List<Set<Point>> shipPieces;

    public ChooseShipPieceState(List<Set<Point>> shipPieces, ShipBoard shipBoard) {
        this.shipPieces = shipPieces;
        this.shipBoard = shipBoard;
    }

    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            shipBoard.keepShipPiece(shipPieces,0,true);
            getNextState();
        }
    }

    @Override
    public synchronized void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        shipBoard.keepShipPiece(shipPieces,pieceIndex,true);
        getNextState();
    }

    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }

    public synchronized List<Set<Point>> getShipPieces() {
        return shipPieces;
    }
}
