package it.polimi.ingsw.galaxytruckers.client.model;

import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.util.*;

/**
 * Represents the flight board in the Galaxy Truckers game.
 * The flight board is a track on which player ships advance during the flight phase of the game.
 * It maintains the positions of all ships on the board and defines the length of the track.
 */
public class FlightBoard {
    /** Maps each ship to its current position on the flight board */
    private final Map<ShipBoard, Integer> shipToPlace;

    /** The total length of the flight board track */
    private final int loopLength;

    /** List of valid starting positions for ships on the flight board */
    private final List<Integer> startingPositions;

    /**
     * Constructs a new flight board with specified starting positions and length.
     *
     * @param startingPositions List of valid starting positions for ships
     * @param loopLength The total length of the flight board track
     */
    public FlightBoard(List<Integer> startingPositions, int loopLength) {
        this.shipToPlace = new HashMap<>();
        this.startingPositions = startingPositions;
        this.loopLength = loopLength;
    }

    /**
     * Sets the positions of all ships on the flight board.
     * This method replaces all current ship positions with the provided mapping.
     *
     * @param shipToPlace A map containing ships and their positions
     */
    public void setShipToPlace(Map<ShipBoard, Integer> shipToPlace) {
        this.shipToPlace.clear();
        this.shipToPlace.putAll(shipToPlace);
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
    public int getLoopLength() {
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
