package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SecondDeck extends Deck{

    List<List<AdventureCard>> forecastDecks;
    List<AdventureCard> hiddenDeck;

    public SecondDeck() throws IOException {
        super(loadRelevantCards(Set.of(Level.TEST, Level.FIRST, Level.SECOND)));
        List<AdventureCard> easyCards = relevantCards.stream()
                .filter(c -> c.getCardLevel() == Level.TEST || c.getCardLevel() == Level.FIRST)
                .limit(8)
                .toList();
        List<AdventureCard> hardCards = relevantCards.stream()
                .filter(c -> c.getCardLevel() == Level.SECOND)
                .limit(4)
                .toList();
        forecastDecks = IntStream.range(0, 3)
                .mapToObj(i -> {
                    List<AdventureCard> forecastDeck = new ArrayList<>();
                    forecastDeck.addAll(easyCards.subList(i, i+1));
                    forecastDeck.addAll(hardCards.subList(i*2, i*2+2));
                    return forecastDeck;
                })
                .toList();
        hiddenDeck.addAll(easyCards.subList(4, 5));
        hiddenDeck.addAll(hardCards.subList(8, 10));
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
