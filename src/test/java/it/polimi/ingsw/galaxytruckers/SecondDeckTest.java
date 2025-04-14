package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.SecondDeck;
import it.polimi.ingsw.galaxytruckers.model.SecondFlightBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SecondDeckTest {
    SecondDeck secondDeck;
    FlightBoard flightBoard;

    @BeforeEach
    void setUp() throws IOException {
        flightBoard = new SecondFlightBoard(Set.of());
        secondDeck = new SecondDeck();
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