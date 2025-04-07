package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;


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
        if (pieceIndex < 0 || pieceIndex >= shipPieces.size()) {
            throw new IllegalArgumentException("Invalid piece index");
        }
        List<Point> componentsToRemove = IntStream.range(0, shipPieces.size())
                .filter(x -> x != pieceIndex)
                .mapToObj(shipPieces::get)
                .flatMap(Collection::stream)
                .toList();
        for (Point p : componentsToRemove) {
            shipBoard.removeComponent(p);
        }
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }
}
