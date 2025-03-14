import shipBuilding.ShipBoard;

public class TestFactory implements GameFactory{
    @Override
    public Deck createDeck() {
        return new TestDeck();
    }

    @Override
    public FlightBoard createFlightBoard() {
        return new TestFlightBoard();
    }

    @Override
    public ShipBoard createShipBoard() {
        return null;
        // TODO : implement method
        // return new ShipBoard();
    }
}