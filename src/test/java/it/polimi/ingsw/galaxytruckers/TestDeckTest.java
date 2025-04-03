package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckTest extends JavaFXInitializer {
    Deck testDeck;
    FlightBoard flightBoard;

    @Test
    void masterDeckHasTheEightTestCards() throws IOException {
        flightBoard = new SecondFlightBoard(Set.of());
        testDeck = new TestDeck(flightBoard);
        assertEquals(8, testDeck.getMasterDeck().size());
    }
}