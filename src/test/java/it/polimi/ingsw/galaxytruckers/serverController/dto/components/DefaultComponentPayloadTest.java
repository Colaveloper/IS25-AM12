package it.polimi.ingsw.galaxytruckers.serverController.dto.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultComponentPayloadTest {
    ComponentPayload componentPayload = new ComponentPayload() {};

    @Test
    void getCrewType() {
        assertNull(componentPayload.crewType());
    }

    @Test
    void getNumResidents() {
        assertEquals(0, componentPayload.numResidents());
    }

    @Test
    void active() {
        assertFalse(componentPayload.active());
    }

    @Test
    void goods() {
        assertNull(componentPayload.goods());
    }

    @Test
    void numBatteries() {
        assertEquals(0, componentPayload.numBatteries());
    }
}