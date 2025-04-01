package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.ShipBoardStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    protected Component myComponent;
    protected List<Connector> myConnectors;
    protected ShipBoardStub myShipBoard;

    @BeforeEach
    void setUp() {
        myConnectors = new ArrayList<>(Arrays.asList(
                Connector.NONE,
                Connector.UNIVERSAL,
                Connector.SINGLE,
                Connector.DOUBLE
        ));
        myComponent = new Component(null, myConnectors);
        myShipBoard = new ShipBoardStub();
    }

    @Test
    void getConnectors() {
        assertEquals(myConnectors, myComponent.getConnectors());
    }

    @Test
    void orientationChangesAndLoops() {
        assertEquals(0, myComponent.getOrientation());

        // Rotating left should loop back
        for (int i = 1; i <= 10; i++) {
            myComponent.rotateLeft();
            assertEquals(i % 4, myComponent.getOrientation());
        }
    }
}
