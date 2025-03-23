package it.polimi.ingsw.galaxytruckers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

//    @Test
//    void initMasterDeck() {
//    }
//
//    @Test
//    void getCurrentCard() {
//    }
//
//    @Test
//    void drawCard() {
//    }
//
//    @Test
//    void peekForecastDeck() {
//    }

    @Test
    void loadComponents() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/main/resources/cards.json");

        List<AdventureCard> deck = Deck.loadCards(jsonFile);

        assertNotNull(deck);
        assertFalse(deck.isEmpty());

        assertEquals(1, deck.size());

//        assertEquals("PiratesCard", deck.getFirst().getClass().getSimpleName());
    }
}