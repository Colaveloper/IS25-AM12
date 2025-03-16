package it.polimi.ingsw.galaxytruckers;// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import it.polimi.ingsw.galaxytruckers.Deck;
import it.polimi.ingsw.galaxytruckers.TempDeck;
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;

import java.util.List;

public class GameModel {
    private Deck deck;
    private AdventureCard activeCard;
    private int maxCardPlays; //TEMPORARY, defines the maximum times a card can be played
    private int activeCardPlayCount;

    //for testing, using the TempDeck, not an official Deck
    public GameModel(){
        this.deck = new TempDeck();
        maxCardPlays = 4;
        activeCardPlayCount = 0;
    }

    //CARD-RELATED METHODS
    public void drawCard(){
        activeCard = deck.drawCard();
        activeCardPlayCount = 0;
    }

    public void resetSteps(){
        activeCard.resetSteps();
    }

    public List<CardState> getCardStates(){
        return activeCard.getChoicesList();
    }

    public String getCardName() {
        return activeCard.getName();
    }

    public int getCardSacrifice() {
        return activeCard.getSacrifice();
    }

    public int getCardCredits() {
        return activeCard.getCredits();
    }

    public int getCardFlightDaysLost() {
        return activeCard.getFlightDaysLost();
    }

    public CardState getCardState() {
        return activeCard.nextStep(this);
    }

    public void passCardToNextPlayer(){
        activeCardPlayCount++;
        if(activeCardPlayCount < 4){
            activeCard.resetSteps();    //reset only if there is another player
            //TODO: pass the card to next player
            System.out.println("Card has been passed to the next player.");
        }
        else {
            System.out.println("Card can't be played anymore");
        }

    }

    public int getShipPower() {
        //TODO: get actual shipboard model to do this
        return 2;
    }

    public void epidemic() {
        //TODO: all players lose 1 crew member in paired cabins
        System.out.println("Epidemic strikes!");
    }

    public void loseResidents(int numResidents){
        //TODO: update current player shipboard to reflect lost residents
        System.out.println("current player has lost " + numResidents + " residents");
    }
    public void grabCredits(int credits){
        //TODO: update current player shipboard to reflect gain in credits
        System.out.println("current player has received " + credits + " credits");
    }
    public void loseFlightDays(int flightDaysLost){
        //TODO: make current player lose flight days
        //or potentially all players depending on card
        System.out.println("current player has lost " + flightDaysLost + " days");
    }

    public int getExposedConnectors() {
        //TODO: use shipboard method to get exposed connectors
        System.out.println("current player has 3 exposed connectors");
        return 3;
    }

    public void loseGoods(int goods){
        System.out.println("current player has lost " + goods + " goods");
    }

    public void loseFlightDaysLeastResidents(int flightDaysLost){
        //TODO: select the player with least number of residents to lose flight days
        System.out.println("Player with least number of residents has lost " + flightDaysLost + " days");
    }

//    public int throwDice(Boolean activatable) {
//        i = rand;
//        if(activatable) {
//            shipboard.setDice(i);
//        }
//    }
}
