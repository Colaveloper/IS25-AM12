package it.polimi.ingsw.galaxytruckers.state;

import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;


//TODO : this state used also in ship-building,
// we could make adventureCard nullable and change state accordingly
public class ChooseShipPieceState extends GameState {
    private final ShipBoard shipBoard;
    private final List<Set<Point>> shipPieces;
    private int pieceIndex;

    public ChooseShipPieceState(List<Set<Point>> shipPieces, ShipBoard shipBoard) {
        this.shipPieces = shipPieces;
        this.shipBoard = shipBoard;
        this.pieceIndex = -1;
    }

    @Override
    public void chooseShipPiece(int pieceIndex) {
        if (pieceIndex < 0 || pieceIndex >= shipPieces.size()) {
            throw new IllegalArgumentException("Invalid piece index");
        }
        this.pieceIndex = pieceIndex;
    }

    @Override
    public GameState getNextState() {
        if (pieceIndex == -1) {
            throw new IllegalStateException("No piece selected");
        }
        for (int i = 0; i < shipPieces.size(); i++) {
            if (i != pieceIndex) {
                for (Point p : shipPieces.get(i)) {
                    shipBoard.removeComponent(p);
                }
            }
        }
        return adventureCard.nextStep();
    }
}
