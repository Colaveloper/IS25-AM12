import shipBuilding.ShipBoard;

public class TestFactory implements GameFactory{
    @Override
    public TestDeck createDeck() {
        return new TestDeck();
    }

    @Override
    public FlightBoard createFlightBoard() {
        return new TestFlightBoard();
    }

    @Override
    public ShipBoard createShipBoard() {
        return null;
        // return new TestShipBoard();
        // TODO : implement method
    }
}