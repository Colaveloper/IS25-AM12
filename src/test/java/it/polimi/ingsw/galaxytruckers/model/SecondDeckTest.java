package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SecondDeckTest {
    SecondDeck secondDeck;
    FlightBoard flightBoard;
    Game game;

    @BeforeEach
    void setUp() throws IOException {
        flightBoard = new SecondFlightBoard(0);
        game = new Game(Level.SECOND) {
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
    void initMasterDeck() {

    }
}