package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;

import java.util.List;

public abstract class Deck{
    protected List<AdventureCard> masterDeck;
    private AdventureCard currentCard;

    public AdventureCard getCurrentCard() {
        return currentCard;
    }

    public List<AdventureCard> getForecastDeck(int id) {
        throw new UnsupportedOperationException("This action is unsupported at the selected level");
    }

    public void initMasterDeck() {
        throw new UnsupportedOperationException("This action is unsupported at the selected level");
    }

    /**
     * Removes a card from the master deck and sets it as current
     * @return true if there are still cards, false otherwise
     */
    public boolean tryDrawCard() {
        if (masterDeck.isEmpty()) {
            return false;
        } else {
            currentCard = masterDeck.removeFirst();
            return true;
        }
    }
}
