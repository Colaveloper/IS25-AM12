package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.*;
import java.util.stream.Collectors;

public abstract class FlightBoard {
    protected final int loopLength;
    protected final Deque<Integer> startingPositionsLeft;
    protected final Set<ShipBoard> allShips; // contains playing+dead ships
    protected final Map<ShipBoard, Integer> shipToPlace; // contains playing ships only
    protected final Map<ShipBoard, Integer> finalScores; // can be populated early by giving up

    public FlightBoard(Level level, Set<ShipBoard> allShips) {
        this.loopLength = level.getLoopLength();
        this.startingPositionsLeft = level.getStartingPositions();
        this.allShips = allShips;
        this.shipToPlace = new HashMap<>();
        this.finalScores = new HashMap<>();
    }

    public Map<ShipBoard, Integer> getShipToPlace() {
        return shipToPlace;
    }

    // return value to be interpreted as "building phase is finished for everybody"
    public abstract boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition);

    public List<ShipBoard> getOrderedShips() {
        return shipToPlace.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        // nth .pop() returns the nth player
    }

    public abstract Set<ShipBoard> getLappedShips ();

    public void displaceShip (ShipBoard shipBoard, int displacement) {
        int displacementLeft = displacement;
        int tryMove = displacementLeft>0 ? 1 : -1;
        int newPosition = shipToPlace.get(shipBoard);
        while (displacementLeft!=0) {
            if (shipToPlace.containsValue(newPosition + tryMove)) {
                tryMove += displacementLeft>0 ? 1 : -1;
            } else {
                newPosition += tryMove;
                displacementLeft += displacementLeft>0 ? -1 : 1;
            }
        }
        shipToPlace.put(shipBoard, newPosition);
    }

    public void removeShip (ShipBoard shipBoard) {
        shipToPlace.remove(shipBoard);
    }

    public Map<ShipBoard, Integer> getFinalScores () {

        int minExposedConnectors = allShips.stream()
                .filter(ship -> !finalScores.containsKey(ship)) // who gave up does not count!
                .mapToInt(ShipBoard::getExposedConnectorsNumber)
                .min()
                .orElse(-1); // impossible case: there have to be players...

        allShips.forEach(ship -> {
            if (!finalScores.containsKey(ship)) { // who gave up already knows his score
                finalScores.put(ship,
                        ship.getGoodsValue() + ship.getCredits() - ship.getLosses() +
                        // finish order reward
                        (shipToPlace.containsKey(ship) ? 4 - getOrderedShips().indexOf(ship) : 0) +
                        // best looking ship reward
                        (ship.getExposedConnectorsNumber() == minExposedConnectors ? 2 :0 ));
            }
        });

        return finalScores;
    }

    public abstract void giveUp(ShipBoard shipBoard);
}
