package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.*;

public class FlightBoard {
    private final Map<ShipBoard, Integer> shipToPlace;
    private final int loopLength;
    private final List<Integer> startingPositions;

    public FlightBoard(Level level) {
        this.shipToPlace = new HashMap<>();
        switch (level) {
            case TEST -> {
                this.startingPositions = Arrays.asList(4, 2, 1, 0);
                this.loopLength = 18;
            }
            case SECOND -> {
                this.startingPositions = Arrays.asList(6, 3, 1, 0);
                this.loopLength = 24;
            }
            case FIRST -> {
                throw new IllegalArgumentException("You are trying to create a flightboard for" +
                        "level " + level.toString() + ", but that is not implemented yet.");
            }
            case null, default -> {
                throw new IllegalArgumentException("You have provided an invalid level argument" +
                        " to the FlightBoard constructor");
            }
        }

    }

    /**
     * @return a map containing for each ship on the flight board its position
     * */
    public Map<ShipBoard, Integer> getShipToPlace() {
        return shipToPlace;
    }

    /**
     * Places the given ship on the flight board at the given position
     *
     * @param shipBoard the ship to be placed
     * @param position the new shipPosition
     */
    public void setShipPosition(ShipBoard shipBoard, int position) {
        shipToPlace.put(shipBoard, position);
    }

    /**
     * @return the flightboard's length*/
    protected int getLoopLength() {
        return loopLength;
    }

    /**
     * Removes a set of shipboards from the flightboard
     *
     * @param shipBoard the {@link Set} of ships to remove
     */
    public void removeShip(ShipBoard shipBoard) {
        shipToPlace.remove(shipBoard);
    }

    /**
     * @return a {@link List} of the starting positions
     * on the flight board
     * */
    public List<Integer> getStartingPositions() {
        return startingPositions;
    }
}


