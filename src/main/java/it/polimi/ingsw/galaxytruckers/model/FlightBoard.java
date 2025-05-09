package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FlightBoard {
    protected final Set<ShipBoard> allShips; // contains playing+dead ships
    protected final Map<ShipBoard, Integer> shipToPlace; // contains playing ships only
    protected List<Integer> startingPositionsLeft;

    public FlightBoard(Set<ShipBoard> allShips) {
        this.allShips = allShips;
        this.shipToPlace = new HashMap<>();
    }

    public Set<ShipBoard> getAllShips() {
        return allShips;
    }

    public Map<ShipBoard, Integer> getShipToPlace() {
        return shipToPlace;
    }

    // return value to be interpreted as "building phase is finished for everybody"
    public abstract boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition);

    protected abstract int getLoopLength();

    public List<ShipBoard> getOrderedShips() {
        return shipToPlace.entrySet().stream()
                .sorted(Comparator.<Map.Entry<ShipBoard, Integer>>comparingInt(Map.Entry::getValue).reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        // nth .pop() returns the nth player
    }

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

    public void removeShips (Set<ShipBoard> shipsToRemove) {
        throw new UnsupportedOperationException("Not available for this type of FlightBoard");
    }

    public Set<ShipBoard> getLappedShips() {
        throw new UnsupportedOperationException("Not available for this type of FlightBoard");
    }

    @VisibleForTesting
    public List<Integer> getStartingPositionsLeft() {
        return new ArrayList<>(startingPositionsLeft);
    }
}


