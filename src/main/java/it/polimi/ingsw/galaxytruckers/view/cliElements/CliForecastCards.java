package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.List;

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

    public void setCards(List<AdventureCard> cards) {
        forecastDeck = cards;
        setDirty();
    }
}
