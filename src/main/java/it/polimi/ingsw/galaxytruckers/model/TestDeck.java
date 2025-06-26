package it.polimi.ingsw.galaxytruckers.model;

import java.io.IOException;

/**
 * TestDeck is a subclass of Deck used for games of level TEST.
 */
public class TestDeck extends Deck{
    public TestDeck(Game game) throws IOException {
        super(game);
        super.masterDeck = relevantCards;
    }
}
