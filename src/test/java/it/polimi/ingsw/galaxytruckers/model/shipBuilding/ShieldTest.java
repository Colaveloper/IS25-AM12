package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTest extends ComponentTest {

    private Shield myShield;

    @BeforeEach
    void setUp() {
        super.setUp();
        myShield = new Shield(myConnectors);
    }

    @Test
    void protectedDirectionChangesWithRotation() {
        myShield.activate(myShipBoard);
        for (int i = 0; i < 4; i++) {
            myShield.setOrientation(i);
            assertArrayEquals(new int[]{i%4, (i+1)%4}, myShield.getDefensibleDirections());
        }
    }
}