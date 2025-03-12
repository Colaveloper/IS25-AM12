package it.polimi.ingsw.galaxytruckers.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    private Component myComponent;
    private List<Connector> myConnectors;

    @BeforeEach
    void setUp() {
        myConnectors = new ArrayList<>(Arrays.asList(
                Connector.NONE,
                Connector.UNIVERSAL,
                Connector.SINGLE,
                Connector.DOUBLE
        ));
        myComponent = new Component(myConnectors);
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
                () -> new Component(tooFew));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Component(tooMany));
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
