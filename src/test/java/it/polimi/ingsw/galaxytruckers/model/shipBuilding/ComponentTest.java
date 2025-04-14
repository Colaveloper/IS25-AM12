package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.ShipBoardStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        myComponent = new Component(myConnectors);
        myShipBoard = new ShipBoardStub();
    }

    @Test
    void getConnectors() {
        assertEquals(myConnectors, myComponent.getConnectors());
    }

    @Test
    void setOrientationUpdatesConnectorsOrder() {
        assertEquals(0, myComponent.getOrientation());

        myComponent.setOrientation(1);
        assertEquals(1, myComponent.getOrientation());
        List<Connector> expConnectors = new ArrayList<>(myConnectors);
        Collections.rotate(expConnectors,1);
        assertEquals(expConnectors, myComponent.getConnectors());
    }
}
