package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.IOException;
import java.util.Set;

public class TestDeck extends Deck{
    public TestDeck(Game game) throws IOException {
        super(game);
        super.masterDeck = relevantCards;
    }
}
