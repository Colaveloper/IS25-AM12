package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

public class SecondFactory implements GameFactory{
    @Override
    public Deck createDeck() {
        return new SecondDeck();
    }

    @Override
    public FlightBoard createFlightBoard() {
        return null;
        // TODO : implement method
        // return new SecondFlightBoard(...);
    }

    @Override
    public ShipBoard createShipBoard() {
        return null;
        // TODO : implement method
        // return new ShipBoard(...);
    }
}