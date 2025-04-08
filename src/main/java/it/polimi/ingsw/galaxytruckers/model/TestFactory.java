package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.TestShipBoard;

import java.io.IOException;
import java.util.Set;

public class TestFactory implements GameFactory{
    @Override
    public Deck createDeck() throws IOException {
        return new TestDeck();
    }

    @Override
    public FlightBoard createFlightBoard(Set<ShipBoard> allShips) {
         return new TestFlightBoard(allShips);
    }

    @Override
    public ShipBoard createShipBoard(ComponentBank componentBank, Colors color) {
        return new TestShipBoard(componentBank, color);
    }
}