package it.polimi.ingsw.galaxytruckers.shipBuilding;

import it.polimi.ingsw.galaxytruckers.ShipBoardStub;
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
    void constructorThrowsExceptionForInvalidConnectorList() {

        List<Connector> tooFew = new ArrayList<>(Arrays.asList(
                Connector.SINGLE,
                Connector.DOUBLE));

        List<Connector> tooMany = new ArrayList<>(Arrays.asList(
                Connector.SINGLE,
                Connector.DOUBLE,
                Connector.UNIVERSAL,
                Connector.NONE,
                Connector.SINGLE
        ));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Component(null, tooFew));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Component(null, tooMany));
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

//    TODO: implement this tests
//    @Test
//    void addToVisitor() {
//    }
//
//    TODO: implement this tests
//    @Test
//    void removeFromVisitor() {
//    }
}
