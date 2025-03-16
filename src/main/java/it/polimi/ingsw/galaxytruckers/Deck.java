package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;

import java.util.List;

public abstract class Deck{
    private List<AdventureCard> masterDeck;
    private AdventureCard currentCard;

    abstract public void initMasterDeck();

    abstract public AdventureCard getCurrentCard();

    abstract public AdventureCard drawCard();

    abstract public List<AdventureCard> peekForecastDeck(int id);
}
