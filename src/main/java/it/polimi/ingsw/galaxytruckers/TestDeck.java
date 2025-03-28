package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class TestDeck extends Deck{
    public TestDeck(File cardsJson) throws IOException {
        super(loadCards(cardsJson, Set.of(Level.TEST)));
        // TODO: populate masterdeck with the 8 Learning Cards from JSON
    }
}
