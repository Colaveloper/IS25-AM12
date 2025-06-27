package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements;

import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CLI element that displays a deck of forecast cards.
 */
public class CliForecastCards extends CliElement {
    private List<AdventureCard> forecastDeck;

    @Override
    protected List<String> getNewDescription() {
        List<String> newDescription = new ArrayList<>();
        newDescription.add("Cards in the forecast deck:\n");
        for (AdventureCard card : forecastDeck) {
            newDescription.addAll(new CliAdventureCard(card).getDescription());
        }
        return newDescription;
    }

    /**
     * Sets the cards in the forecast deck.
     * @param cards the list of AdventureCard objects to set as the forecast deck
     */
    public void setCards(List<AdventureCard> cards) {
        forecastDeck = cards;
        setDirty();
    }
}
