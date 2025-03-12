package it.polimi.ingsw.galaxytruckers.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CannonTest extends ComponentTest {

    Cannon myCannon;

    @BeforeEach
    void setUp() {
        super.setUp();
        myCannon = new Cannon(myConnectors);
    }

    @Test
    void firePowerConformsWithDirection() {
        assertEquals(2, myCannon.getFirePower());

        for (int i = 1; i <= 3; i++) {
            myCannon.rotateLeft();
            assertEquals(1, myCannon.getFirePower());
        }

        myCannon.rotateLeft();
        assertEquals(2, myCannon.getFirePower());
    }
}