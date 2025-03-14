package it.polimi.ingsw.galaxytruckers.shipBuilding;

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
    void crewInformationIsUnavailableBeforeInitializing() {
        assertThrows(IllegalStateException.class, myCabin::getNumResidents);
        assertThrows(IllegalStateException.class, myCabin::getCrewType);
        assertThrows(IllegalStateException.class, () -> myCabin.loseResidents(1));
    }

    @Test
    void numResidentsConformsWithCrewType() {
        myCabin.initialize(CrewType.PURPLE);
        assertEquals(myCabin.getCrewType(), CrewType.PURPLE);
        assertEquals(myCabin.getNumResidents(), 1);
        myCabin.initialize(CrewType.BROWN);
        assertEquals(myCabin.getCrewType(), CrewType.BROWN);
        assertEquals(myCabin.getNumResidents(), 1);
        myCabin.initialize(CrewType.HUMAN);
        assertEquals(myCabin.getCrewType(), CrewType.HUMAN);
        assertEquals(myCabin.getNumResidents(), 2);
    }

    @Test
    void loseResidents() {
        myCabin.initialize(CrewType.HUMAN);
        assertThrows(IllegalArgumentException.class, () -> myCabin.loseResidents(-1));
        assertThrows(IllegalArgumentException.class, () -> myCabin.loseResidents(3));
        myCabin.loseResidents(2);
        assertEquals(myCabin.getNumResidents(), 0);
        assertThrows(IllegalArgumentException.class, () -> myCabin.loseResidents(1));
    }
}