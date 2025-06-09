package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public final class ChooseShipPieceState extends AdventureState {

    private final List<Set<Point>> shipPieces;

    public ChooseShipPieceState(ShipBoard myShip, List<Set<Point>> shipPieces, ShipBoard currentShip) {
        this.myShip = myShip;
        this.shipPieces = shipPieces;
        this.currentShip = currentShip;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(myShip.equals(currentShip)) actions.add(StateActions.CHOOSE_SHIP_PIECE);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        List<Point> removedPoints = shipBoard.removeShipPiece(shipPieces, pieceIndex);
        game.getObservers().forEach(observer -> observer.notifyChooseShipPiece(shipBoard, pieceIndex, removedPoints));
    }

    public List<Set<Point>> getShipPieces() {
        return shipPieces;
    }
}
