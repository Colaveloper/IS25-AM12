package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.IOException;
import java.util.Set;
import com.google.common.annotations.VisibleForTesting;

public class TestDeck extends Deck{
    public TestDeck(FlightBoard flightBoard) throws IOException {
        super(Set.of(Level.TEST), flightBoard);
        super.masterDeck = relevantCards;
    }
}
