import shipBuilding.ShipBoard;

public interface GameFactory {
    Deck createDeck();
    FlightBoard createFlightBoard();
    ShipBoard createShipBoard();
}