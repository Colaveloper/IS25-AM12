package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EngineTest extends ComponentTest {

    Engine myEngine;

    @BeforeEach
    void setUp() {
        super.setUp();
        myEngine = new Engine(myConnectors);
    }

    @Test
    void isValidOnlyFacingUp() {
        assertTrue(myEngine.isValid());
        myEngine.setOrientation(1);
        assertFalse(myEngine.isValid());
    }

    @Test
    void getEnginePower() {
        assertEquals(1, myEngine.getEnginePower());
    }
}
