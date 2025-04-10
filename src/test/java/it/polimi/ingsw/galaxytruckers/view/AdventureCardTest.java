package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AdventureCardTest {
    /*
    * current tests are temporary tests to quickly check
    * functionality
    * TODO: create more comprehensive tests for this class
    * */
    @Test
    void describePlanetsGeneratesCorrectStringForCard16() throws IOException {
        String expected = """
                planet 1: blue, blue, blue, green, red
                planet 2: blue, yellow, red
                planet 3: green, red""";

        AdventureCard card = new AdventureCard(16); // using a random id doesn't matter for this test
        String actual = card.describePlanets();

        assertEquals(expected, actual);
    }

    @Test
    void describeGoodsOutputsCorrectStringForCard32() throws IOException {
        String expected = """
                blue, green, yellow""";

        AdventureCard card = new AdventureCard(32); // using a random id doesn't matter for this test
        String actual = card.describeGoods();

        assertEquals(expected, actual);
    }
}