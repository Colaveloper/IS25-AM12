package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LifeSupportTest extends ComponentTest {

    LifeSupport myBrownLifeSupport;
    LifeSupport myPurpleLifeSupport;

    @BeforeEach
    void setUp() {
        super.setUp();
        myBrownLifeSupport = new LifeSupport(null, myConnectors, CrewType.BROWN);
        myPurpleLifeSupport = new LifeSupport(null, myConnectors, CrewType.PURPLE);
    }

    @Test
    void constructorThrowsExceptionForHumanCrewType() {
        assertThrows(IllegalArgumentException.class, () -> new LifeSupport(null, myConnectors, CrewType.HUMAN));
    }

    @Test
    void getAlienType() {
        assertEquals(CrewType.BROWN, myBrownLifeSupport.getAlienType());
        assertEquals(CrewType.PURPLE, myPurpleLifeSupport.getAlienType());
    }
}