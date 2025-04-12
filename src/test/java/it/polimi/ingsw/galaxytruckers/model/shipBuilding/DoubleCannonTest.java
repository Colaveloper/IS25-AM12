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
    @Override
    void firePowerConformsWithDirection() {

        for (int i = 1; i <= 4; i++) {
            myDoubleCannon.rotateLeft();
            assertEquals(0, myDoubleCannon.getFirePower());
        }

        myDoubleCannon.activate(myShipBoard);
        assertEquals(4, myDoubleCannon.getFirePower());
        for (int i = 1; i <= 3; i++) {
            myDoubleCannon.rotateLeft();
            assertEquals(2, myDoubleCannon.getFirePower());
        }
        myDoubleCannon.rotateLeft();
        assertEquals(4, myDoubleCannon.getFirePower());

        myDoubleCannon.deactivate(myShipBoard);
        for (int i = 1; i <= 4; i++) {
            myDoubleCannon.rotateLeft();
            assertEquals(0, myDoubleCannon.getFirePower());
        }
    }
}