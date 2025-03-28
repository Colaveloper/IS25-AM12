package it.polimi.ingsw.galaxytruckers;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FlightBoard implements Physical {
    protected final Set<ShipBoard> allShips; // contains playing+dead ships
    protected final Map<ShipBoard, Integer> shipToPlace; // contains playing ships only
    @VisibleForTesting
    protected final Map<ShipBoard, Integer> finalScores; // can be populated early by giving up
    protected List<Integer> startingPositionsLeft;

    public FlightBoard(Set<ShipBoard> allShips) {
        this.allShips = allShips;
        this.shipToPlace = new HashMap<>();
        this.finalScores = new HashMap<>();
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

    // TODO: create a local variable to store this data
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
                    shipToPlace.entrySet().stream()
                            .map(e->e.getValue())
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
    };

    public void giveUp(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("Not available for this type of FlightBoard");
    };

    public Set<ShipBoard> getAndRemoveLappedShips() {
        throw new UnsupportedOperationException("Not available for this type of FlightBoard");
    };

    public Map<ShipBoard, Integer> getFinalScores() {
        assignFinishOrderReward();
        assignFinishOrderReward();
        countGoodsValue();
        countCreditsAndLosses();
        return finalScores;
    }

    @VisibleForTesting
    protected void assignBestLookingShipReward() {
        int minExposedConnectors = shipToPlace.keySet().stream()// who gave up does not count!
                .mapToInt(ShipBoard::getExposedConnectorsNumber)
                .min()
                .orElse(0); // no ship on board

        shipToPlace.keySet().forEach(s ->
                finalScores.merge(s, s.getExposedConnectorsNumber() == minExposedConnectors ? 2 : 0, Integer::sum)
        );
    }

    @VisibleForTesting
    protected void assignFinishOrderReward() {
        shipToPlace.keySet().forEach(s ->
                finalScores.merge(s, 4 - getOrderedShips().indexOf(s), Integer::sum)
        );
    }

    @VisibleForTesting
    protected void countCreditsAndLosses() {
        shipToPlace.keySet().forEach(s ->
                finalScores.merge(s, s.getCredits() - s.getLosses(), Integer::sum)
        );
    }

    @VisibleForTesting
    protected void countGoodsValue() {
        allShips.forEach(s -> {
            if (shipToPlace.containsKey(s)) {
                finalScores.merge(s, s.getGoodsValue(), Integer::sum);
            } else {
                finalScores.merge(s, (s.getGoodsValue()+1)/2, Integer::sum);
            }
        });
    }

    @Override
    public String getDescription() {
        return ""; // TODO: describe
    }
}


