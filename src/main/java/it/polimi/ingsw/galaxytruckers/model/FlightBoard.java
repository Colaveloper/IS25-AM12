package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FlightBoard {
    protected final Map<ShipBoard, Integer> shipToPlace; // contains playing ships only
    protected List<Integer> startingPositionsLeft;

    public FlightBoard() {
        this.shipToPlace = new HashMap<>();
    }

    /**
     * @return a map containing for each ship still in play their position
     * on the flightBoard
     * */
    public Map<ShipBoard, Integer> getShipToPlace() {
        return shipToPlace;
    }

    /**
     * Puts the given ship in the given starting position, if available, and makes that
     * position unavailable for future calls.
     *
     * @param shipBoard the ship to be placed
     * @param startingPosition the starting position of the ship to place
     * @return true if there are no more starting positions left, false otherwise
     * */
    public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        return placeShipOnFlightBoard(shipBoard);
    }

    /**
     * Puts the given ship in the first available starting position and makes that
     * position unavailable for future calls.
     *
     * @param shipBoard the ship to be placed
     * @return true if there are no more starting positions left, false otherwise
     * */
    public boolean placeShipOnFlightBoard(ShipBoard shipBoard) {
        shipToPlace.put(shipBoard, startingPositionsLeft.removeFirst());
        // to be interpreted as "building phase is finished for everybody"
        return startingPositionsLeft.isEmpty();
    }

    /**
     * @return the flightboard's length*/
    protected abstract int getLoopLength();

    /**
     * @return a {@link List} of shipboards in the order that they appear
     * on the flightboard*/
    public List<ShipBoard> getOrderedShips() {
        return shipToPlace.entrySet().stream()
                .sorted(Comparator.<Map.Entry<ShipBoard, Integer>>comparingInt(Map.Entry::getValue).reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        // nth .pop() returns the nth player
    }

    /**
     * Moves the specified shipboard by displacement value on the flightboard
     * @param shipBoard the shipboard to be moved
     * @param displacement the amount of spaces to move on the flightboard*/
    public void displaceShip (ShipBoard shipBoard, int displacement) {
        int displacementLeft = displacement;
        int tryMove = displacementLeft>0 ? 1 : -1;
        int newPosition = shipToPlace.get(shipBoard);
        while (displacementLeft!=0) {
            int finalNewPosition = newPosition;
            int finalTryMove = tryMove;
            if (
                    shipToPlace.values().stream()
                            .anyMatch(p -> p%getLoopLength() == (finalNewPosition + finalTryMove))
            ) {
                tryMove += displacementLeft>0 ? 1 : -1;
            } else {
                newPosition += tryMove;
                tryMove = displacementLeft>0 ? 1 : -1;
                displacementLeft += displacementLeft>0 ? -1 : 1;
            }
        }
        shipToPlace.put(shipBoard, newPosition);
    }

    /**
     * Removes a set of shipboards from the flightboard
     * @param shipsToRemove the {@link Set} of ships to remove*/
    public void removeShips (Set<ShipBoard> shipsToRemove) {
        throw new UnsupportedOperationException("Not available for this type of FlightBoard");
    }

    /**
     * @return a {@link Set} of shipboards that have been lapped on
     * the flightboard*/
    public Set<ShipBoard> getLappedShips() {
        throw new UnsupportedOperationException("Not available for this type of FlightBoard");
    }

    /**
     * @return a {@link List} of the remaining starting positions
     * on the flightboard*/
    @VisibleForTesting
    public List<Integer> getStartingPositionsLeft() {
        return new ArrayList<>(startingPositionsLeft);
    }
}


