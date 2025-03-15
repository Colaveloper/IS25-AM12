package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.List;
import java.util.Set;

public class TestFlightBoard extends FlightBoard{
    public TestFlightBoard(Level level, Set<ShipBoard> allShips) {
        super(level, allShips);
    }

    @Override
    public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        shipToPlace.put(shipBoard, startingPositionsLeft.pop());
        // to be interpreted as "building phase is finished for everybody"
        return startingPositionsLeft.size() + allShips.size() == 4;
    }

    @Override
    public Set<ShipBoard> getLappedShips() {
        return Set.of();
    }

    @Override
    public void giveUp(ShipBoard shipBoard) {
        throw new IllegalArgumentException("cannot give up in test flight");
    }
}
