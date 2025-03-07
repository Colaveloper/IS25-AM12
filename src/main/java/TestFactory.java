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
        return new TestShipBoard();
    }
}