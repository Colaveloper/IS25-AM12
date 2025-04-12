package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;


// TODO : this state used also in ship-building,
//  we could make adventureCard nullable and change state accordingly
public class ChooseShipPieceState extends GameState {
    private final ShipBoard shipBoard;
    private final List<Set<Point>> shipPieces;

    public ChooseShipPieceState(List<Set<Point>> shipPieces, ShipBoard shipBoard) {
        this.shipPieces = shipPieces;
        this.shipBoard = shipBoard;
    }

    @Override
    public void chooseShipPiece(int pieceIndex) {
        removeOtherShipPieces(shipBoard, pieceIndex, shipPieces);
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }
}
