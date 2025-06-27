package it.polimi.ingsw.galaxytruckers.client.model.factory;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.client.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.TestShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.TestShipBuildingState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestFactoryTest {

    @Test
    void createFlightBoardInstantiatesCorrectly() {
        TestFactory factory = new TestFactory();
        FlightBoard board = factory.createFlightBoard();
        assertNotNull(board);
        assertEquals(18, board.getLoopLength());
        assertEquals(java.util.Arrays.asList(4, 2, 1, 0), board.getStartingPositions());
    }

    @Test
    void createShipBoardInstantiatesCorrectly() {
        TestFactory factory = new TestFactory();
        TestShipBoard board = (TestShipBoard) factory.createShipBoard(GameColor.BLUE);
        assertNotNull(board);
        assertEquals(GameColor.BLUE, board.getColor());
    }

    @Test
    void createShipBuildingStateIsNotNull() {
        TestFactory factory = new TestFactory();
        TestShipBuildingState state = (TestShipBuildingState) factory.createShipBuildingState();
        assertNotNull(state);
    }
}