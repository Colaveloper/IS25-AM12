package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SecondDeck extends Deck{

    List<List<AdventureCard>> forecastDecks;
    List<AdventureCard> hiddenDeck;

    public SecondDeck(File cardsJson) throws IOException {
        super(loadCards(cardsJson, Set.of(Level.TEST, Level.FIRST, Level.SECOND)));
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
