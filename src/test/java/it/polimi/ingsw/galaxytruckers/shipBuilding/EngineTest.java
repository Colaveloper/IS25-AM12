package it.polimi.ingsw.galaxytruckers.shipBuilding;

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
        for (int i = 0; i < 5; i++) {
            if (i%4 == 0) {
                assertTrue(myEngine.isValid());
            } else {
                assertFalse(myEngine.isValid());
            }
            myEngine.rotateLeft();
        }
    }

    @Test
    void getEnginePower() {
        assertEquals(1, myEngine.getEnginePower());
    }
}
