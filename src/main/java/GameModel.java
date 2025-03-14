// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import adventureCards.utils.Choice;
import adventureCards.AdventureCard;

import static adventureCards.utils.Choice.GRAB_CREDITS;

public class GameModel {
    private Deck deck;
    private AdventureCard activeCard;

    public AdventureCard drawCard(){
        activeCard = deck.drawCard();
        return activeCard;
    }

    public int throwDice(Boolean activatable) {
        i = rand;
        if(activatable) {
            shipboard.setDice(i);
        }
    }
}
