package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryTest extends ComponentTest {

    private Battery myBattery;

    @BeforeEach
    void setUp() {
        super.setUp();
        myBattery = new Battery(myConnectors, 3);
    }

    @Test
    void constructorThrowsExceptionForNotAllowedNumBatteries() {
        assertThrows(IllegalArgumentException.class, () -> new Battery(myConnectors, 1));
        assertThrows(IllegalArgumentException.class, () -> new Battery(myConnectors, 4));
    }

    @Test
    void numBatteriesDecreasesIfPossibleOrThrowsException() {
        assertEquals(3, myBattery.getNumBatteries());
        myBattery.useBatteries();
        assertEquals(2, myBattery.getNumBatteries());
    }

    @Test
    void useBatteriesThrowsExceptionForNotAllowedNumBatteries() {
        int numBatteries = myBattery.getNumBatteries();
        for (int i = 0; i < numBatteries; i++) {
            myBattery.useBatteries();
        }
        assertThrows(IllegalArgumentException.class, () -> myBattery.useBatteries());
    }
}