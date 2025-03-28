package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Level;

import java.io.IOException;
import java.util.Set;

public class TestDeck extends Deck{
    public TestDeck() throws IOException {
        super(loadRelevantCards(Set.of(Level.TEST)));
        // TODO: populate masterdeck with the 8 Learning Cards from JSON
    }
}
