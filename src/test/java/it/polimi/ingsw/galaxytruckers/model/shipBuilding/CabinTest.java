package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CabinTest extends ComponentTest {

    private Cabin myCabin;

    @BeforeEach
    void setUp() {
        super.setUp();
        myCabin = new Cabin(myConnectors);
    }

    @Test
    void numResidentsConformsWithCrewType() {
        myCabin.initialize(CrewType.PURPLE);
        assertEquals(CrewType.PURPLE, myCabin.getCrewType());
        assertEquals(1, myCabin.getNumResidents());
        myCabin.initialize(CrewType.BROWN);
        assertEquals(CrewType.BROWN, myCabin.getCrewType());
        assertEquals(1, myCabin.getNumResidents());
        myCabin.initialize(CrewType.HUMAN);
        assertEquals(CrewType.HUMAN, myCabin.getCrewType());
        assertEquals(2, myCabin.getNumResidents());
    }

    @Test
    void loseResidents() {
        myCabin.initialize(CrewType.HUMAN);
        myCabin.loseResidents();
        assertEquals(1, myCabin.getNumResidents());
        myCabin.loseResidents();
        assertThrows(IllegalStateException.class, () -> myCabin.loseResidents());
    }
}