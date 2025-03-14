package it.polimi.ingsw.galaxytruckers;

public interface GameFactory {
    Deck createDeck();
    FlightBoard createFlightBoard();
    ShipBoard createShipBoard();
}