package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

public interface GameFactory {
    Deck createDeck();
    FlightBoard createFlightBoard();
    ShipBoard createShipBoard();
}