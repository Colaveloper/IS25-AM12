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
        this.startingPositionsLeft = new ArrayList<>(Level.SECOND.getStartingPositions());
    }

    @Override
    public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        // TODO: consider concurrent access to startingPositionsLeft !!!
        if (!startingPositionsLeft.contains(startingPosition)) {
            throw new IllegalArgumentException("Position not available for start");
        } else {
            startingPositionsLeft.remove(startingPositionsLeft.indexOf(startingPosition));
            shipToPlace.put(shipBoard, startingPosition);
            // to be interpreted as "building phase is finished for everybody"
            return startingPositionsLeft.size() + allShips.size() == 4;
        }
    }

    @Override
    public Set<ShipBoard> getLappedShips() {
        return shipToPlace.entrySet().stream()
                .filter(entry -> shipToPlace.get(getOrderedShips().getFirst()) - entry.getValue() > loopLength)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public void giveUp(ShipBoard shipBoard) {
        removeShip(shipBoard);
        finalScores.put(shipBoard, (shipBoard.getGoodsValue() + 1) / 2 + shipBoard.getCredits() - shipBoard.getLosses());
        if (allShips.size() - finalScores.size() == 1) {
            // TODO: one-player is left!
            //  ignore the Combat Zone and Sabotage adventures
        }
    }
}
