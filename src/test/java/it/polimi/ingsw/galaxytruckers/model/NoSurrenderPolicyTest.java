package it.polimi.ingsw.galaxytruckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class NoSurrenderPolicyTest {
    NoSurrenderPolicy policy = new NoSurrenderPolicy();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        System.setErr(new PrintStream(baos));
    }

    @Test
    void isSurrenderEnabled() {
        assertFalse(policy.isSurrenderEnabled());
    }

    @Test
    void requestSurrenderLogsError() {
        policy.requestSurrender(null,null);
        assertEquals("Requesting surrender when surrender is disabled", baos.toString().trim());
        policy.getSurrenderedShips();
        policy.setEventListener(null);
    }

    @Test
    void confirmSurrender() {
        policy.confirmSurrender(null);
        assertEquals("Confirming surrender when surrender is disabled", baos.toString().trim());
    }

    @Test
    void getSurrenderedShips() {
        policy.getSurrenderedShips();
        assertEquals("Getting surrendered ships when surrender is disabled", baos.toString().trim());
    }
}