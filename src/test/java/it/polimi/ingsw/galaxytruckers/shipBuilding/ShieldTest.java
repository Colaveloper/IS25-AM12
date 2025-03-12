package it.polimi.ingsw.galaxytruckers.shipBuilding;

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
    void protectedDirectionChangesWithActivationAndRotation() {
        assertArrayEquals(new int[]{}, myShield.getProtectedDirections());
//      TODO: make the activation logic testable by implementing shipboard
//      myShield.activate();
    }
}