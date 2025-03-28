package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Level;

import java.io.IOException;
import java.util.Set;

public class TestDeck extends Deck{
    public TestDeck(FlightBoard flightBoard) throws IOException {
        super(Set.of(Level.TEST), flightBoard);
        masterDeck = relevantCards;
    }
}
