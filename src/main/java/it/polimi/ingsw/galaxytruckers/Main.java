package it.polimi.ingsw.galaxytruckers;/*
* MAIN CLASS SIMULATING THE CONTROLLER CALLING MODEL METHODS
* THIS IS FOR TESTING PURPOSES ONLY
* */
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String args[]){
        GameModel model = new GameModel();
        Scanner userScanner = new Scanner(System.in);
        String input;
        CardState cardState;
        model.drawCard();
        cardState = model.getCardState();

        //simulating drawing the AbandonedShip card

        //List<CardState> activeCardState = model.getCardStates();
        System.out.println("The " + model.getCardName() + " card has been drawn\nResident sacrifice: " +
                model.getCardSacrifice() + "\nCredit gains: " + model.getCardCredits()  +
                "\nFlight days cost: " + model.getCardFlightDaysLost());


        while(cardState != CardState.END_CARD){
            switch (cardState){
                case ASK_NEXT_PLAYER:
                    //this is simulating the controller passing this onto the view,
                    //which will ask for user input
                    System.out.println("Do you wish to pass this card to the next player?(y/n): ");
                    input = userScanner.nextLine();

                    if (input.equals("y")) {
                        model.resetSteps();
                        model.passCardToNextPlayer();
                        break;
                     } else if (input.equals("n")) {
                        //continue with the switch case
                    } else {
                        System.out.println("Invalid input.");
                        System.out.println("Do you wish to pass this card to the next player?(y/n): ");
                        input = userScanner.nextLine();
                    }
                    break;
                case LOSE_RESIDENT:
                    model.loseResidents(model.getCardSacrifice());//makes the current player lose residents
                    break;
                default:
                    System.out.println("Error in processing card choice");
            }
            cardState = model.getCardState();
        }
    }
}
