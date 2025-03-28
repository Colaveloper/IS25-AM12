package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.io.IOException;
import java.util.Set;

public interface GameFactory {
    Deck createDeck() throws IOException;
    FlightBoard createFlightBoard(Set<ShipBoard> allShips);
    ShipBoard createShipBoard(ComponentBank componentBank, Colors color);
}