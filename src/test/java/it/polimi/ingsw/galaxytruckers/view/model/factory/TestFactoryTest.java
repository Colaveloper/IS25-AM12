package it.polimi.ingsw.galaxytruckers.view.model.factory;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.TestShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.TestShipBuildingState;
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