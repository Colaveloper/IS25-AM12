package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.TestShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.model.state.ShipCorrectionState;
import it.polimi.ingsw.galaxytruckers.model.state.TestShipBuildingState;

import java.io.IOException;

public class TestFactory implements GameFactory{
    @Override
    public Deck createDeck(Game game) throws IOException {
        return new TestDeck(game);
    }

    @Override
    public FlightBoard createFlightBoard(int shipsN) {
         return new TestFlightBoard(shipsN);
    }

    @Override
    public ShipBoard createShipBoard(GameColor color) {
        return new TestShipBoard(color);
    }

    @Override
    public ShipBuildingState createShipBuildingState() {
        return new TestShipBuildingState();
    }

    @Override
    public ShipCorrectionState createShipCorrectionState() {
        return new ShipCorrectionState(false);
    }

    @Override
    public SurrenderPolicy createSurrenderPolicy() {
        return new NoSurrenderPolicy();
    }
}