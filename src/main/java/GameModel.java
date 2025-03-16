// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import adventureCards.utils.Choice;
import adventureCards.AdventureCard;

import static adventureCards.utils.Choice.GRAB_CREDITS;

public class GameModel {
    private Deck deck;
    private AdventureCard activeCard;

    //TEMPORARY CODE, FOR TESTING ONLY ---------------------------------------------
    public GameModel(){
        this.deck = new TempDeck();
    }

    //CARD-RELATED METHODS
    public AdventureCard drawCard(){
        activeCard = deck.drawCard();
        return activeCard;
    }

    public void passCardToNextPlayer(){
        //TODO: pass the card to next player
        System.out.println("Card has been passed to the next player.");
    }

    public void loseResidents(int numResidents){
        //TODO: update current player shipboard to reflect lost residents
        System.out.println("current player has lost " + numResidents + " residents");
    }
    public void grabCredits(int credits){
        //TODO: update current player shipbpard to reflect gain in credits
        System.out.println("current player has received " + credits + " credits");
    }
    public void loseFlightDays(int flightDaysLost){
        //TODO: make currecnt player lose flight days
        //or potentially all players depending on card
        System.out.println("current player has lost " + flightDaysLost + " days");
    }

//    public int throwDice(Boolean activatable) {
//        i = rand;
//        if(activatable) {
//            shipboard.setDice(i);
//        }
//    }
}
