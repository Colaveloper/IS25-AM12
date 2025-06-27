package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Represents a state in which the player can choose a ship piece to keep.
 * This state is part of the adventure phase where players select pieces for their ship
 * when it becomes disconnected or damaged.
 */
public final class ChooseShipPieceState extends AdventureState implements GameStateInterface {
    private final ShipBoard shipBoard;
    private final List<Set<Point>> shipPieces;

    /**
     * Constructor for ChooseShipPieceState.
     *
     * @param shipPieces the pieces available for the player to choose from
     * @param shipBoard  the ship board of the player who is choosing a piece
     */
    public ChooseShipPieceState(List<Set<Point>> shipPieces, ShipBoard shipBoard) {
        this.shipPieces = shipPieces;
        this.shipBoard = shipBoard;
    }

    /**
     * Skips the current state if it's the given ship board's turn.
     *
     * @param shipBoard the ship board of the player who wants to skip
     */
    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            shipBoard.keepShipPiece(shipPieces, 0, true);
            getNextState();
        }
    }

    /**
     * Allows the player to choose a ship piece to keep. Calls {@link ShipBoard#keepShipPiece(List, int, boolean)}
     * to remove all other ship pieces and keep the selected one.
     *
     * @param shipBoard  the ship board of the player
     * @param pieceIndex the index of the ship piece to remove
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        shipBoard.keepShipPiece(shipPieces, pieceIndex, true);
        getNextState();
    }

    /**
     * @return the ship board of the player who is choosing a piece
     */
    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }

    /**
     * @return the ship pieces available for the player to choose from
     */
    public synchronized List<Set<Point>> getShipPieces() {
        return shipPieces;
    }
}
