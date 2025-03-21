package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SecondFlightBoard extends FlightBoard{
    public SecondFlightBoard(Set<ShipBoard> allShips) {
        super(allShips);
        this.loopLength = Level.SECOND.getLoopLength();
        this.startingPositionsLeft = new ArrayList<>(Level.SECOND.getStartingPositions().subList(0, allShips.size()));
    }

    @Override
    public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        // TODO: consider concurrent access to startingPositionsLeft !!!
        if (!startingPositionsLeft.contains(startingPosition)) {
            throw new IllegalArgumentException("Position not available for start");
        } else {
            startingPositionsLeft.remove((Integer) startingPosition);
            shipToPlace.put(shipBoard, startingPosition);
            // to be interpreted as "building phase is finished for everybody"
            return startingPositionsLeft.size() + allShips.size() == 4;
        }
    }

    @Override
    public void removeShips (Set<ShipBoard> shipsToRemove) {
        shipToPlace.entrySet().removeIf(entry -> shipsToRemove.contains(entry.getKey()));
        shipsToRemove.forEach(ship ->finalScores.put(ship, (ship.getGoodsValue() + 1) / 2 + ship.getCredits() - ship.getLosses()));
    }

    @Override
    public Set<ShipBoard> getAndRemoveLappedShips() {
        System.out.println(shipToPlace.entrySet().stream()
                .filter(entry -> shipToPlace.get(getOrderedShips().getFirst()) - entry.getValue() > loopLength).toString());

        Set<ShipBoard> lappedShips = shipToPlace.entrySet().stream()
                .filter(entry -> shipToPlace.get(getOrderedShips().getFirst()) - entry.getValue() > loopLength)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        removeShips(lappedShips);

        return lappedShips;
    }

    @Override
    public void giveUp(ShipBoard shipBoard) {
        removeShips(Set.of(shipBoard));
        if (allShips.size() - finalScores.size() == 1) {
            // TODO: one-player is left!
            //  ignore the Combat Zone and Sabotage adventures
        }
    }
}
