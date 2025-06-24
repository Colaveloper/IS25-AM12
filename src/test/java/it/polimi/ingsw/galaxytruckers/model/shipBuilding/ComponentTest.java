package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    protected Component myComponent;
    protected Map<Direction, Connector> myConnectors;
    protected SecondShipBoardForTesting myShipBoard;

    @BeforeEach
    void setUp() {
        myConnectors = new HashMap<>(Map.of(
                Direction.UP, Connector.NONE,
                Direction.LEFT, Connector.UNIVERSAL,
                Direction.DOWN, Connector.SINGLE,
                Direction.RIGHT, Connector.DOUBLE
        ));
        myComponent = new Component(myConnectors);
        myShipBoard = new SecondShipBoardForTesting();
    }

    @Test
    void getConnectors() {
        assertEquals(myConnectors, myComponent.getConnectors());
    }

    @Test
    void setOrientationUpdatesConnectorsOrder() {
        assertEquals(Direction.UP, myComponent.getOrientation());
        Map<Direction, Connector> rotatedConnectors = Direction.rotateDirectionMap(myConnectors, Direction.UP, Direction.LEFT);

        myComponent.setOrientation(Direction.LEFT); // 1
        assertEquals(Direction.LEFT, myComponent.getOrientation());
        assertEquals(rotatedConnectors, myComponent.getConnectors());
    }
}
