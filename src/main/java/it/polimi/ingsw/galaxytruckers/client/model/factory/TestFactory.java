package it.polimi.ingsw.galaxytruckers.client.model.factory;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.client.model.*;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.TestShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.client.model.state.TestShipBuildingState;

import java.util.Arrays;
import java.util.List;

public class TestFactory extends GameFactory {
    private final static List<Integer> flightBoardStartingPositions = Arrays.asList(4, 2, 1, 0);
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