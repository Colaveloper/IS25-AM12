package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.io.IOException;
import java.util.Set;

public interface GameFactory {
    Deck createDeck() throws IOException;
    FlightBoard createFlightBoard(Set<ShipBoard> allShips);
    ShipBoard createShipBoard(ComponentBank componentBank, Colors color);
    Hourglass createHourglass();
}