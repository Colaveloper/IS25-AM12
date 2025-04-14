package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleCannonTest extends CannonTest {

    private DoubleCannon myDoubleCannon;

    @BeforeEach
    void setUp() {
        super.setUp();
        myDoubleCannon = new DoubleCannon(myConnectors);
    }

    @Test
    void firePowerIsPositiveOnlyWhenActive() {
        assertEquals(0, myDoubleCannon.getFirePower());
        myDoubleCannon.activate(myShipBoard);
        assertEquals(4, myDoubleCannon.getFirePower());
    }

    @Test
    @Override
    void firePowerConformsWithDirection() {
        myDoubleCannon.activate(myShipBoard);
        assertEquals(4, myDoubleCannon.getFirePower());
        myDoubleCannon.setOrientation(1);
        assertEquals(2, myDoubleCannon.getFirePower());
    }
}