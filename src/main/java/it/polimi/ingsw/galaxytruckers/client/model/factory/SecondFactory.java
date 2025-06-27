package it.polimi.ingsw.galaxytruckers.client.model.factory;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.client.model.*;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.SecondShipBuildingState;
import it.polimi.ingsw.galaxytruckers.client.model.state.ShipBuildingState;

import java.util.Arrays;
import java.util.List;

public class SecondFactory extends GameFactory {
    private final static List<Integer> flightBoardStartingPositions = Arrays.asList(6, 3, 1, 0);
    private final static int flightBoardLoopLength = 24;

    @Override
    public FlightBoard createFlightBoard() {
        return new FlightBoard(flightBoardStartingPositions, flightBoardLoopLength);
    }

    @Override
    public ShipBoard createShipBoard(GameColor color) {
         return new SecondShipBoard(color);
    }

    @Override
    public ShipBuildingState createShipBuildingState() {
        return new SecondShipBuildingState();
    }
}