package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents the state where players choose which piece of their damaged ship to keep
 * in the Galaxy Truckers game. This occurs when a ship is damaged and broken into
 * multiple disconnected pieces, requiring the player to choose one piece to continue with.
 */
public final class ChooseShipPieceState extends AdventureState implements GameStateInterface, AdventureStateInterface {

    /** List of disconnected ship pieces, each represented as a set of points */
    private final List<Set<Point>> shipPieces;

    /**
     * Creates a new ChooseShipPieceState with the specified parameters.
     * Initializes the state with the player's ship, the list of disconnected ship pieces,
     * and the currently active ship.
     *
     * @param myShip The ship board of the local player
     * @param shipPieces List of disconnected ship pieces, each represented as a set of points
     * @param currentShip The ship board that is currently active
     */
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

    /**
     * Gets the list of disconnected ship pieces.
     * Each piece is represented as a set of points on the ship board.
     * The player must choose one of these pieces to keep, while the others will be discarded.
     *
     * @return List of disconnected ship pieces
     */
    public List<Set<Point>> getShipPieces() {
        return shipPieces;
    }
}
