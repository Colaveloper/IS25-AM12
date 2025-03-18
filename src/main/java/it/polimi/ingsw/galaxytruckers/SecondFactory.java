package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

public class SecondFactory implements GameFactory{
    @Override
    public Deck createDeck() {
        return null;
        // TODO : implement method
        // return new SecondDeck();
    }

    @Override
    public FlightBoard createFlightBoard() {
        return null;
        // TODO : implement method
        // return new SecondFlightBoard(...);
    }

    @Override
    public ShipBoard createShipBoard(ComponentBank componentBank, Colors color) {
        return null;
        // TODO : implement method
        // return new SecondShipBoard(...);
    }
}