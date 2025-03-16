//TEMPORARY CLASS, TESTING ONLY

import adventureCards.AbandonedShipCard;
import adventureCards.AdventureCard;

import java.util.List;

public class TempDeck extends Deck{
    @Override
    public AdventureCard drawCard() {
        //for testing purposes only
        //generates an AbanondedShipCard
        AbandonedShipCard card = new AbandonedShipCard(4, 3, 1);
        return card;
    }

    @Override
    public void initMasterDeck() {

    }
    @Override
    public List<AdventureCard> peekForecastDeck(int id) {
        return null;
    }
    @Override
    public AdventureCard getCurrentCard() {
        return null;
    }


}
