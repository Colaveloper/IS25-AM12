package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SecondDeckTest extends JavaFXInitializer {
    SecondDeck secondDeck;
    FlightBoard flightBoard;

    @BeforeEach
    void setUp() throws IOException {
        flightBoard = new SecondFlightBoard(Set.of());
        secondDeck = new SecondDeck(flightBoard);
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