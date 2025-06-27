package it.polimi.ingsw.galaxytruckers.server.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class representing a flight board in the Galaxy Truckers game.
 */
public abstract class FlightBoard {
    protected final Map<ShipBoard, Integer> shipToPlace; // contains playing ships only
    protected final List<Integer> startingPositionsLeft;
    protected GameEventListener gameEventListener = null;

    /**
     * Constructor for FlightBoard.
     *
     * @param gameEventListener the listener to notify about flight board updates
     */
    public FlightBoard(GameEventListener gameEventListener) {
        this.shipToPlace = new HashMap<>();
        this.startingPositionsLeft = new ArrayList<>();
        this.gameEventListener = gameEventListener;
    }

    /**
     * Sets up the flight board with the given ship boards, placing
     * them all on the flight board.
     *
     * @param shipBoards the set of ship boards to place on the flight board
     */
    public void setup(Set<ShipBoard> shipBoards) {
        for (ShipBoard shipBoard : shipBoards) {
            placeInternal(shipBoard);
        }
    }

    private int placeInternal(ShipBoard shipBoard) {
        int position;
        synchronized (startingPositionsLeft) {
            position = startingPositionsLeft.removeFirst();
        }
        synchronized (shipToPlace) {
            shipToPlace.put(shipBoard, position);
        }
        return position;
    }

    /**
     * @return a map containing for each ship still in play their position
     * on the flightBoard
     */
    public Map<ShipBoard, Integer> getShipToPlace() {
        Map<ShipBoard, Integer> res;
        synchronized (shipToPlace) {
            res = new HashMap<>(shipToPlace);
        }
        return res;
    }

    /**
     * calls {@link #placeShipOnFlightBoard(ShipBoard)}
     */
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        placeShipOnFlightBoard(shipBoard);
    }

    /**
     * Puts the given ship in the first available starting position and makes that
     * position unavailable for future calls.
     *
     * @param shipBoard the ship to be placed
     */
    public void placeShipOnFlightBoard(ShipBoard shipBoard) {
        int position = placeInternal(shipBoard);
        gameEventListener.notifyFlightBoardUpdateEvent(shipBoard, position);
    }

    /**
     * @return the flightboard's length
     */
    protected abstract int getLoopLength();

    /**
     * @return a {@link List} of shipboards in the order that they appear
     * on the flightboard
     */
    public List<ShipBoard> getOrderedShips() {
        List<ShipBoard> res;
        synchronized (shipToPlace) {
            res = shipToPlace.entrySet().stream()
                    .sorted(Comparator.<Map.Entry<ShipBoard, Integer>>comparingInt(Map.Entry::getValue).reversed())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        return res;
    }

    /**
     * Moves the specified shipboard by displacement value on the flightboard
     *
     * @param shipBoard    the shipboard to be moved
     * @param displacement the amount of spaces to move on the flightboard
     */
    public void displaceShip(ShipBoard shipBoard, int displacement) {
        int newPosition;
        synchronized (shipToPlace) {
            int displacementLeft = displacement;
            int tryMove = displacementLeft > 0 ? 1 : -1;
            newPosition = shipToPlace.get(shipBoard);
            while (displacementLeft != 0) {
                int finalNewPosition = newPosition;
                int finalTryMove = tryMove;
                if (
                        shipToPlace.values().stream()
                                .anyMatch(p -> p % getLoopLength() == (finalNewPosition + finalTryMove))
                ) {
                    tryMove += displacementLeft > 0 ? 1 : -1;
                } else {
                    newPosition += tryMove;
                    tryMove = displacementLeft > 0 ? 1 : -1;
                    displacementLeft += displacementLeft > 0 ? -1 : 1;
                }
            }
            shipToPlace.put(shipBoard, newPosition);
            gameEventListener.notifyFlightBoardUpdateEvent(shipBoard, newPosition);
        }
    }

    /**
     * Removes a set of shipboards from the flightboard
     *
     * @param shipsToRemove the {@link Set} of ships to remove
     * @throws UnsupportedOperationException if the method is not available for this type of FlightBoard
     */
    public void removeShips(Set<ShipBoard> shipsToRemove) {
        throw new UnsupportedOperationException("Not available for this type of FlightBoard");
    }

    /**
     * @return a {@link Set} of shipboards that have been lapped on
     * the flightboard
     * @throws UnsupportedOperationException if the method is not available for this type of FlightBoard
     */
    public Set<ShipBoard> getLappedShips() {
        throw new UnsupportedOperationException("Not available for this type of FlightBoard");
    }

    /**
     * @return a {@link List} of the remaining starting positions
     * on the flightboard
     */
    @VisibleForTesting
    public List<Integer> getStartingPositionsLeft() {
        List<Integer> res;
        synchronized (startingPositionsLeft) {
            res = new ArrayList<>(startingPositionsLeft);
        }
        return res;
    }
}


