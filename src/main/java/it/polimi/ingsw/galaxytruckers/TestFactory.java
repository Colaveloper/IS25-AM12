package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

public class TestFactory implements GameFactory{
    @Override
    public Deck createDeck() {
        return new TestDeck();
    }

    @Override
    public FlightBoard createFlightBoard() {
        return null;
        // TODO : implement method
        // return new TestFlightBoard(...);
    }

    @Override
    public ShipBoard createShipBoard(ComponentBank componentBank, Colors color) {
        return null;
        // TODO : implement method
        // return new ShipBoard(...);
    }
}