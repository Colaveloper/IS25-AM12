package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.view.observables.Invalidator;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.*;

public class FlightBoard implements Invalidator {
    private final Map<ShipBoard, Integer> shipToPlace;
    private final int loopLength;
    private final List<Integer> startingPositions;

    private final List<Listener> listeners = new ArrayList<>();

    public FlightBoard(List<Integer> startingPositions, int loopLength) {
        this.shipToPlace = new HashMap<>();
        this.startingPositions = startingPositions;
        this.loopLength = loopLength;
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
        notifyObservers();
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
        notifyObservers();
    }

    /**
     * @return a {@link List} of the starting positions
     * on the flight board
     * */
    public List<Integer> getStartingPositions() {
        return startingPositions;
    }

    public void notifyObservers() {
        for (Listener o : listeners) {
            o.onNotified();
        }
    }

    @Override
    public void addObserver(Listener o) {
        listeners.add(o);
    }

    @Override
    public void removeObserver(Listener o) {
        listeners.remove(o);
    }
}


