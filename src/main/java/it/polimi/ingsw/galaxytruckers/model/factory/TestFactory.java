package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.TestShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.TestShipBuildingState;

import java.io.IOException;
import java.util.Set;

public class TestFactory extends GameFactory{
    @Override
    public Deck createDeck(Game game) throws IOException {
        return new TestDeck(game);
    }

    @Override
    public FlightBoard createFlightBoard(Set<ShipBoard> allShips) {
         return new TestFlightBoard(allShips);
    }

    @Override
    public ShipBoard createShipBoard(FourColors color) {
        return new TestShipBoard(color);
    }

    @Override
    public GameState createFirstGameState() {
        return new TestShipBuildingState();
    }
}