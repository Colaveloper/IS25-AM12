import java.util.List;

public abstract class Deck{
    private List<AdventureCard> masterDeck;
    private AdventureCard currentCard;

    abstract public void initMasterDeck();

    abstract public AdventureCard getCurrentCard();

    abstract public void drawCard();

    abstract public List<AdventureCard> peekForecastDeck(int id);
}
