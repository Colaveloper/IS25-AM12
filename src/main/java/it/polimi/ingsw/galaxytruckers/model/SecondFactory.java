package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.io.IOException;
import java.util.Set;

public class SecondFactory implements GameFactory{
    @Override
    public Deck createDeck(FlightBoard flightBoard) throws IOException {
        return new SecondDeck(flightBoard);
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