package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SecondDeckTest {
    SecondDeck secondDeck;
    FlightBoard flightBoard;
    Game game;

    @BeforeEach
    void setUp() throws IOException {
        flightBoard = new SecondFlightBoardForTesting(0);
        game = new GameStub(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }
        };
        secondDeck = new SecondDeck(game);
    }

    @Test
    void getForecastDeck() {
        for (int i = 0 ; i<3 ; i++) {
            assertEquals(3, secondDeck.getForecastDeck(i).size());
        }
    }

    @Test
    void initMasterDeckThrowsWithInvalidIndex() {
        assertThrows(IllegalArgumentException.class, () -> secondDeck.getForecastDeck(-1));
    }

    @Test
    void initMasterDeck() {
        secondDeck.initMasterDeck();
        secondDeck.drawCard();
        assertEquals(Level.SECOND, secondDeck.getCurrentCard().getCardLevel());
    }
}