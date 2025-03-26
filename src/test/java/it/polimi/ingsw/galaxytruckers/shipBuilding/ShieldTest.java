package it.polimi.ingsw.galaxytruckers.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTest extends ComponentTest {

    private Shield myShield;

    @BeforeEach
    void setUp() {
        super.setUp();
        myShield = new Shield(null, myConnectors);
    }

    @Test
    void protectedDirectionChangesWithRotation() {
        myShield.activate(myShipBoard);
        for (int i = 1; i <= 4; i++) {
            myShield.rotateLeft();
            assertArrayEquals(new int[]{i%4, (i+1)%4}, myShield.getDefensibleDirections());
        }
    }
}