package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckTest {
    Game game = new Game(Level.TEST);
    Deck testDeck;
    FlightBoard flightBoard;

    @Test
    void masterDeckHasTheEightTestCards() throws IOException {
        flightBoard = new SecondFlightBoard(0);
        testDeck = new TestDeck(game);
        assertEquals(8, testDeck.getMasterDeck().size());
    }
}