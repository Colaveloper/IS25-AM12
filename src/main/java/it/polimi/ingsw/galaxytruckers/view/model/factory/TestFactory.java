package it.polimi.ingsw.galaxytruckers.view.model.factory;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.*;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.TestShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.view.model.state.TestShipBuildingState;

import java.util.Arrays;
import java.util.List;

public class TestFactory extends GameFactory {
    private final static List<Integer> flightBoardStartingPositions = Arrays.asList(6, 3, 1, 0);
    private final static int flightBoardLoopLength = 18;

    @Override
    public FlightBoard createFlightBoard() {
         return new FlightBoard(flightBoardStartingPositions, flightBoardLoopLength);
    }

    @Override
    public ShipBoard createShipBoard(GameColor color) {
        return new TestShipBoard(color);
    }

    @Override
    public ShipBuildingState createShipBuildingState() {
        return new TestShipBuildingState();
    }
}