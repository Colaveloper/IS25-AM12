package it.polimi.ingsw.galaxytruckers.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryTest extends ComponentTest {

    private Battery myBattery;

    @BeforeEach
    void setUp() {
        super.setUp();
        myBattery = new Battery(null, myConnectors, 3);
    }

    @Test
    void constructorThrowsExceptionForNotAllowedNumBatteries() {
        assertThrows(IllegalArgumentException.class, () -> new Battery(null, myConnectors, 1));
        assertThrows(IllegalArgumentException.class, () -> new Battery(null,myConnectors, 4));
    }

    @Test
    void numBatteriesDecreasesIfPossibleOrThrowsException() {
        assertEquals(3, myBattery.getNumBatteries());
        assertThrows(IllegalArgumentException.class, () -> myBattery.useBatteries(4));
        assertEquals(3, myBattery.getNumBatteries());
        assertThrows(IllegalArgumentException.class, () -> myBattery.useBatteries(0));
        assertEquals(3, myBattery.getNumBatteries());
        myBattery.useBatteries(2);
        assertEquals(1, myBattery.getNumBatteries());
        myBattery.useBatteries(1);
        assertEquals(0, myBattery.getNumBatteries());
    }


}