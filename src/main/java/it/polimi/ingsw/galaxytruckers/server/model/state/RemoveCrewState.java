package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Represents the state of the game where a player must remove crew members from their ship.
 */
public final class RemoveCrewState extends AdventureState implements GameStateInterface {
    private int crewSacrifice;
    private final ShipBoard shipBoard;

    /**
     * Constructor for RemoveCrewState.
     *
     * @param crewSacrifice the number of crew members to be sacrificed
     * @param shipBoard     the ship board of the player removing crew members
     */
    public RemoveCrewState(int crewSacrifice, ShipBoard shipBoard) {
        this.crewSacrifice = crewSacrifice;
        this.shipBoard = shipBoard;
    }

    /**
     * Sets the game associated with this state. Moves to the next
     * state if the ship has no crew members left.
     *
     * @param game the game to associate with this state
     */
    @Override
    public void setGame(Game game) {
        super.setGame(game);
        if (shipBoard.getCrewSize() == 0) getNextState();
    }

    /**
     * If it's the player's turn, it skips the current state by sacrificing crew members.
     *
     * @param shipBoard the ship board of the player who wants to skip
     */
    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            Iterator<Point> positions = new HashSet<>(shipBoard.getCabins().keySet()).iterator();
            Point currentPosition = positions.next();
            while (crewSacrifice > 0) {
                try {
                    shipBoard.loseCrew(currentPosition);
                    crewSacrifice--;
                } catch (IllegalStateException e) {
                    if (positions.hasNext()) currentPosition = positions.next();
                    else {
                        getNextState();
                        return;
                    }
                }
            }
            getNextState();
        }
    }

    /**
     * Removes a crew member from the ship board at the specified position. Moves
     * to the next state if the ship has no crew members left or if there are
     * no more crew members to sacrifice.
     *
     * @param shipBoard the ship board losing the crew
     * @param position  the position of the crew to remove
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void loseCrew(ShipBoard shipBoard, Point position) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        shipBoard.loseCrew(position);
        crewSacrifice--;
        if (crewSacrifice <= 0 || shipBoard.getCrewSize() <= 0) {
            getNextState();
        }
    }

    /**
     * @return the ship board of the player whose turn it is.
     */
    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }

    /**
     * @return the number of crew members that still need to be sacrificed.
     */
    public synchronized int getCrewSacrifice() {
        return crewSacrifice;
    }
}
