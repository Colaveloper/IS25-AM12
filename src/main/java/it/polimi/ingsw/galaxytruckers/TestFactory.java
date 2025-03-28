package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.TestShipBoard;

import java.io.File;
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