package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleEngineTest extends EngineTest {

    private DoubleEngine myDoubleEngine;

    @BeforeEach
    void setUp() {
        super.setUp();
        myDoubleEngine = new DoubleEngine(myConnectors);
    }

    @Test @Override
    void getEnginePower() {
        assertEquals(0, myDoubleEngine.getEnginePower());
        myDoubleEngine.activate();
        assertEquals(2, myDoubleEngine.getEnginePower());
        myDoubleEngine.deactivate();
        assertEquals(0, myDoubleEngine.getEnginePower());
    }
}