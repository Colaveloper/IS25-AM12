package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class SecondDeck extends Deck{
    private final List<List<AdventureCard>> forecastDecks;
    private final List<AdventureCard> hiddenDeck;

    /**
     * Constructor for the SecondDeck class.
     * It initializes the deck with adventure cards of levels TEST, FIRST, and SECOND.
     * The deck is divided into three forecast decks and a hidden deck.
     *
     * @param game the game instance to which this deck belongs
     * @throws IOException if there is an error reading the adventure cards
     */
    public SecondDeck(Game game) throws IOException {
        super(game);
        List<AdventureCard> easyCards = relevantCards.stream()
                .filter(c -> c.getCardLevel() == Level.TEST || c.getCardLevel() == Level.FIRST)
                .limit(4)
                .toList();
        List<AdventureCard> hardCards = relevantCards.stream()
                .filter(c -> c.getCardLevel() == Level.SECOND)
                .limit(8)
                .toList();
        forecastDecks = IntStream.range(0, 3)
                .mapToObj(i -> {
                    List<AdventureCard> forecastDeck = new ArrayList<>();
                    forecastDeck.addAll(easyCards.subList(i, i+1));
                    forecastDeck.addAll(hardCards.subList(i*2, i*2+2));
                    return forecastDeck;
                })
                .toList();
        hiddenDeck = new ArrayList<>();
        hiddenDeck.addAll(easyCards.subList(3, 4));
        hiddenDeck.addAll(hardCards.subList(6, 8));
    }

    @Override
    public List<AdventureCard> getForecastDeck(int deckIndex) {
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
        do {
            Collections.shuffle(masterDeck);
        }
        while (masterDeck.getLast().getCardLevel() != Level.SECOND);
    }
}
