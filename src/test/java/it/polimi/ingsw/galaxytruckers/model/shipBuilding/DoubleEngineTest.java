package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleEngineTest extends EngineTest {

    private DoubleEngine myDoubleEngine;

    @BeforeEach
    void setUp() {
        super.setUp();
        myDoubleEngine = new DoubleEngine(null, myConnectors);
    }

    @Test @Override
    void getEnginePower() {
        assertEquals(0, myDoubleEngine.getEnginePower());
        myDoubleEngine.activate(myShipBoard);
        assertEquals(2, myDoubleEngine.getEnginePower());
        myDoubleEngine.deactivate(myShipBoard);
        assertEquals(0, myDoubleEngine.getEnginePower());
    }
}