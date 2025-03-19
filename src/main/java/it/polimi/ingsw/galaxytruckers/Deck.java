package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCardDeprecated;

import java.util.List;

public abstract class Deck{
    private List<AdventureCardDeprecated> masterDeck;
    private AdventureCardDeprecated currentCard;

    abstract public void initMasterDeck();

    abstract public AdventureCardDeprecated getCurrentCard();

    abstract public AdventureCardDeprecated drawCard();

    abstract public List<AdventureCardDeprecated> peekForecastDeck(int id);
}
