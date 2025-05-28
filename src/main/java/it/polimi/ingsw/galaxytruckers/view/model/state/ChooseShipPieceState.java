package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public final class ChooseShipPieceState extends AdventureState {
    private static List<StateActions> availableActions = List.of(
            StateActions.CHOOSE_SHIP_PIECE
    );

    private final ShipBoard shipBoard;
    private final List<Set<Point>> shipPieces;

    public ChooseShipPieceState(List<Set<Point>> shipPieces, ShipBoard shipBoard) {
        this.shipPieces = shipPieces;
        this.shipBoard = shipBoard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(availableActions);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        shipBoard.removeShipPiece(shipPieces, pieceIndex);
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public List<Set<Point>> getShipPieces() {
        return shipPieces;
    }
}
