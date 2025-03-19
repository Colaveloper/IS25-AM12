package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.Set;

public class SecondFactory implements GameFactory{
    @Override
    public Deck createDeck() {
        return null;
        // TODO : implement method
        // return new SecondDeck();
    }

    @Override
    public FlightBoard createFlightBoard(Set<ShipBoard> allShips) {
        return new SecondFlightBoard(allShips);
    }

    @Override
    public ShipBoard createShipBoard(ComponentBank componentBank, Colors color) {
         return new SecondShipBoard(componentBank, color);
    }
}