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
        return forecastDecks.get(deckIndex);
    }

    @Override
    public void initMasterDeck() {
        forecastDecks.stream()
                .flatMap(Collection::stream)
                .forEach(card -> masterDeck.add(card));
        masterDeck.addAll(hiddenDeck);
        Collections.shuffle(masterDeck);
    }

    @Override
    public List<AdventureCard> peekForecastDeck(int id) {
        return List.of(); // TODO: implement
    }
}
