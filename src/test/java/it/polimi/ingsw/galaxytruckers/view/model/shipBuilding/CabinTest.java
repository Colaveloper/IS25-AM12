package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CabinTest {
    private Map<Direction, Connector> defaultConnectors() {
        Map<Direction, Connector> connectors = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            connectors.put(d, Connector.SINGLE);
        }
        return connectors;
    }

    @Test
    void initialize() {
        Cabin cabin = new Cabin(defaultConnectors(), 1);
        cabin.initialize(CrewType.HUMAN);
        assertEquals(CrewType.HUMAN, cabin.getCrewType());
        assertEquals(2, cabin.getNumResidents());

        cabin.initialize(CrewType.PURPLE);
        assertEquals(CrewType.PURPLE, cabin.getCrewType());
        assertEquals(1, cabin.getNumResidents());

        cabin.initialize(CrewType.BROWN);
        assertEquals(CrewType.BROWN, cabin.getCrewType());
        assertEquals(1, cabin.getNumResidents());
    }

    @Test
    void initializeNullCrewTypeThrows() {
        Cabin cabin = new Cabin(defaultConnectors(), 2);
        assertThrows(IllegalArgumentException.class, () -> cabin.initialize(null));
    }

    @Test
    void loseCrew() {
        Cabin cabin = new Cabin(defaultConnectors(), 3);
        cabin.initialize(CrewType.HUMAN);
        cabin.loseCrew();
        assertEquals(1, cabin.getNumResidents());
        cabin.loseCrew();
        assertEquals(0, cabin.getNumResidents());
    }

    @Test
    void setCrewType() {
        Cabin cabin = new Cabin(defaultConnectors(), 4);
        cabin.setCrewType(CrewType.PURPLE);
        assertEquals(CrewType.PURPLE, cabin.getCrewType());
        cabin.setCrewType(CrewType.BROWN);
        assertEquals(CrewType.BROWN, cabin.getCrewType());
    }

    @Test
    void setNumResidents() {
        Cabin cabin = new Cabin(defaultConnectors(), 5);
        cabin.setNumResidents(7);
        assertEquals(7, cabin.getNumResidents());
        cabin.setNumResidents(0);
        assertEquals(0, cabin.getNumResidents());
    }

    @Test
    void getCrewTypeAndGetNumResidentsInitialValues() {
        Cabin cabin = new Cabin(defaultConnectors(), 6);
        assertEquals(CrewType.HUMAN, cabin.getCrewType());
        assertEquals(0, cabin.getNumResidents());
    }
}