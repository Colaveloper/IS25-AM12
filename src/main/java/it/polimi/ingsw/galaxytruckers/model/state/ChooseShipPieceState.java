package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;


public final class ChooseShipPieceState extends AdventureState {
    private final ShipBoard shipBoard;
    private final List<Set<Point>> shipPieces;

    public ChooseShipPieceState(List<Set<Point>> shipPieces, ShipBoard shipBoard) {
        this.shipPieces = shipPieces;
        this.shipBoard = shipBoard;
    }

    @Override
    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        Set<Point> componentsToRemove = shipBoard.getComponentMap().keySet();
        componentsToRemove.removeAll(shipPieces.get(pieceIndex));
        componentsToRemove.forEach(shipBoard::discardComponent);
        game.getEventListener().notifyShipPieceRemovalEvent(shipBoard,pieceIndex);
        game.setCurrentState(getNextState());
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public List<Set<Point>> getShipPieces() {
        return shipPieces;
    }
}
