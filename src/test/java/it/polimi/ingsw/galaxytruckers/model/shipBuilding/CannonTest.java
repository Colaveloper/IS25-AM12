package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CannonTest extends ComponentTest {

    private Cannon myCannon;

    @BeforeEach
    void setUp() {
        super.setUp();
        myCannon = new Cannon(myConnectors);
    }

    @Test
    void firePowerConformsWithDirection() {
        assertEquals(2, myCannon.getFirePower());
        myCannon.setOrientation(1);
        assertEquals(1, myCannon.getFirePower());
    }
}