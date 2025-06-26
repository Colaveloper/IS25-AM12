package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SecondFlightBoard class represents the second flight board in the Galaxy Truckers game.
 * It extends the FlightBoard class and provides specific functionality for the second flight board,
 * including choosing the starting position on placement and removing ships.
 */
public class SecondFlightBoard extends FlightBoard{
    @VisibleForTesting
    protected static int loopLength;
    @VisibleForTesting
    protected static List<Integer> startingPositions;

    public SecondFlightBoard(int shipsN, GameEventListener gameEventListener) {
        super(gameEventListener);
        loopLength = 24;
        startingPositions = Arrays.asList(6, 3, 1, 0).subList(0, shipsN);
        this.startingPositionsLeft.clear();
        this.startingPositionsLeft.addAll(startingPositions);
    }

    /**
     * Places a ship on the flight board at the specified starting position.
     *
     * @param shipBoard the ship board to place on the flight board
     * @param startingPosition the starting position on the flight board
     * @throws IllegalArgumentException if the starting position is not available
     */
    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        synchronized (startingPositionsLeft) {
            if (!startingPositionsLeft.contains(startingPosition)) {
                throw new IllegalArgumentException("Position not available for positioning");
            }
            startingPositionsLeft.remove((Integer) startingPosition);
        }
        synchronized (shipToPlace) {
            shipToPlace.put(shipBoard, startingPosition);
        }
        gameEventListener.notifyFlightBoardUpdateEvent(shipBoard, startingPosition);
    }

    @Override
    public void removeShips (Set<ShipBoard> shipsToRemove) {
        shipToPlace.entrySet().removeIf(entry -> shipsToRemove.contains(entry.getKey()));
    }

    @Override
    public Set<ShipBoard> getLappedShips() {
        return shipToPlace.entrySet().stream()
                .filter(entry -> shipToPlace.get(getOrderedShips().getFirst()) - entry.getValue() > loopLength)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    protected int getLoopLength() {
        return loopLength;
    }
}
