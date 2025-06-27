package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectorTest {

    @Test
    void matches() {
        assertTrue(Connector.NONE.matches(Connector.NONE));
        assertFalse(Connector.NONE.matches(Connector.UNIVERSAL));
        assertFalse(Connector.UNIVERSAL.matches(Connector.NONE));
        assertTrue(Connector.UNIVERSAL.matches(Connector.SINGLE));
        assertFalse(Connector.SINGLE.matches(Connector.DOUBLE));
        assertTrue(Connector.SINGLE.matches(Connector.SINGLE));
        assertTrue(Connector.SINGLE.matches(Connector.UNIVERSAL));
    }
}