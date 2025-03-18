package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.Set;

public interface GameFactory {
    Deck createDeck();
    FlightBoard createFlightBoard(Set<ShipBoard> allShips);
    ShipBoard createShipBoard(ComponentBank componentBank, Colors color);
}