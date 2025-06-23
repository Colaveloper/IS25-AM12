package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckTest {
    Game game = new Game(Level.TEST);
    Deck deck;
    FlightBoard flightBoard;

    @Test
    void masterDeckHasTheEightTestCards() throws IOException {
        flightBoard = new SecondFlightBoard(0);
        deck = new TestDeck(game);
        assertEquals(8, deck.getMasterDeck().size());
    }

    @Nested
    class PostInitializationTests {
        @BeforeEach
        void setup() throws IOException {
            deck = new TestDeck(game);
            deck.initMasterDeck();
        }

        @Test
        void forecastDecksAreNotImplemented() {
            assertEquals(List.of(),deck.getForecastDeck(0));
        }
    }
}