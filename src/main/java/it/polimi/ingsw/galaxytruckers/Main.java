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

        //simulating drawing the AbandonedShip card
        AdventureCard activeCard = model.drawCard();
        List<CardState> activeCardState = activeCard.getChoicesList();
        System.out.println("The " + activeCard.getName() + " card has been drawn\nResident sacrifice: " +
                activeCard.getSacrifice() + "\nCredit gains: " + activeCard.getCredits()  +
                "\nFlight days cost: " + activeCard.getFlightDaysLost());
        cardState = activeCard.nextStep();

        while(cardState != CardState.END_CARD){
            switch (activeCardState.get(activeCard.getCurrentStep())){
                case ASK_NEXT_PLAYER:
                    //this is simulating the controller passing this onto the view,
                    //which will ask for user input
                    System.out.println("Do you wish to pass this card to the next player?(y/n): ");
                    input = userScanner.nextLine();

                    if (input.equals("y")) {
                        activeCard.resetSteps();
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
                    model.loseResidents(activeCard.getSacrifice());//makes the current player lose residents
                    break;
                case GRAB_CREDITS:
                    model.grabCredits(activeCard.getCredits()); //gives current player credits
                    model.loseFlightDays(activeCard.getFlightDaysLost());
                    break;
                default:
                    System.out.println("Error in processing card choice");
            }
            cardState = activeCard.nextStep();
        }
    }
}
