package it.polimi.ingsw.galaxytruckers;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckTest extends JavaFXInitializer{
    TestDeck testDeck;
    FlightBoard flightBoard;

    @Test
    void masterDeckHasTheEightTestCards() throws IOException {
        flightBoard = new SecondFlightBoard(Set.of());
        testDeck = new TestDeck(flightBoard);
        assertEquals(8, testDeck.masterDeck.size());
    }
}