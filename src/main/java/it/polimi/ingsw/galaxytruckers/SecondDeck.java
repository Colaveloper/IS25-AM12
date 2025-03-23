package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SecondDeck extends Deck{

    List<List<AdventureCard>> forecastDecks;
    List<AdventureCard> hiddenDeck;

    public SecondDeck() {
        // TODO: populate forecastDecks, hiddenDeck with 2 Level.THIRD, 1 Level.SECOND, 1 Level.FIRST from JSON
    }

    @Override
    public List<AdventureCard> getForecastDeck(int deckIndex) {
        // TODO: handle concurrency
        try {
            return forecastDecks.get(deckIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("No forecast deck with such index");
        }

    }

    @Override
    public void initMasterDeck() {
        forecastDecks.stream()
                .flatMap(Collection::stream)
                .forEach(card -> masterDeck.add(card));
        masterDeck.addAll(hiddenDeck);
        Collections.shuffle(masterDeck);
    }
}
