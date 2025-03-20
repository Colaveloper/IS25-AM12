package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestFlightBoard extends FlightBoard{
    public TestFlightBoard(Set<ShipBoard> allShips) {
        super(allShips);
        this.loopLength = Level.TEST.getLoopLength();
        this.startingPositionsLeft = new ArrayList<>(Level.TEST.getStartingPositions());
    }

    @Override
    public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        shipToPlace.put(shipBoard, startingPositionsLeft.removeFirst());
        // to be interpreted as "building phase is finished for everybody"
        return startingPositionsLeft.size() + allShips.size() == 4;
    }
}
