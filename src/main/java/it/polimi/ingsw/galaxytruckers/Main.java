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
        boolean submit = false;
        CardState cardState;
        model.drawCard();
        cardState = model.getCardState();

        //simulating drawing the AbandonedShip card

        //List<CardState> activeCardState = model.getCardStates();
//        System.out.println("The " + model.getCardName() + " card has been drawn\nResident sacrifice: " +
//                model.getCardSacrifice() + "\nCredit gains: " + model.getCardCredits()  +
//                "\nFlight days cost: " + model.getCardFlightDaysLost());


        System.out.println("The " + model.getCardName() + " card has been drawn!");
        while(cardState != CardState.END_CARD){
            switch (cardState){
                case ASK_NEXT_PLAYER:
                    if (model.getCurrentPlayerIndex() < 4) {
                        //this is simulating the controller passing this onto the view,
                        //which will ask for user input
                        System.out.println("Do you wish to pass this card to the next player?(y/n): ");
                        input = userScanner.nextLine();

                        if (input.equals("y")) {
                            //model.resetSteps();
                            model.passCardToNextPlayer();
                            break;
                        } else if (input.equals("n")) {
                            //continue with the switch case
                        } else {
                            System.out.println("Invalid input.");
                            System.out.println("Do you wish to pass this card to the next player?(y/n): ");
                            input = userScanner.nextLine();
                        }
                    }
                    break;
                case LOSE_RESIDENT:
                    model.loseResidents(model.getCardSacrifice());//makes the current player lose residents
                    break;
                case SABOTAGE:
                    model.sabotage();
                    break;
                case ACTIVATE_CANNON:
                    //user input
                    submit = false;
                    while (!submit) {
                        System.out.println("Do you want to activate a CANNON to increase power? (y/n):");
                        input = userScanner.nextLine();
                        if (input.equals("y")) {
                            model.activateCannon(3, 7);
                        } else if (input.equals("n")) {
                            submit = true;
                            //just continues onto next step
                            //model.getCardStates();
                        } else {
                            System.out.println("Invalid input. Going ahead to next step");
                            //model.getCardState();
                        }
                    }
                    break;
                case ACTIVATE_SHIELD:
                    //user input
                    System.out.println("Do you want to activate a SHIELD? (y/n):");
                    input = userScanner.nextLine();
                    model.rollDice();
                    if (input.equals("y")) {
                        System.out.println("Press ENTER to roll the dice");
                        input = userScanner.nextLine();
                        model.activateShield(3, 7);
                    } else if (input.equals("n")) {
                        System.out.println("Press ENTER to roll the dice");
                        input = userScanner.nextLine();
                        model.fireCannonAtPlayer();
                    } else {
                        System.out.println("Invalid input. Going ahead to next step");
                    }
                    break;
                case GRAB_GOODS:
                    model.grabGoods(model.getCardGoods());
                    break;
                case LOSE_GOODS:
                    //TODO: potentially remove this from controller
                    //it may be completely automatic
                    break;
                case START_CARD:
                    System.out.println("start card state");
                    break;
                case GET_BLASTED:
                    System.out.println("get blasted state");
                    break;
                case SUBMIT_POWER:
                    submit = false;
                    System.out.println("submit state");
                    break;
                case CHOOSE_PLANET:
                    int numPlanetInput;
                    //TODO: fix this to account for number of planets available
                    System.out.println("Choose planet (0,1,2)");
                    numPlanetInput = userScanner.nextInt();
                    if(numPlanetInput != 0 && numPlanetInput != 1 && numPlanetInput != 2){
                        System.out.println("Invalid planet to land on. Going to next player");
                        model.passCardToNextPlayer();
                    }
                    else{
                        model.landOnPlanet(numPlanetInput);
                    }
                    break;
                case ACTIVATE_ENGINE:
                    //user input
                    System.out.println("Do you want to activate an ENGINE to increase power? (y/n):");
                    input = userScanner.nextLine();
                    if (input.equals("y")) {
                        model.activateEngine(3, 7);
                    } else if (input.equals("n")) {
                        //just continues onto next step
                        //model.getCardStates();
                    } else {
                        System.out.println("Invalid input. Going ahead to next step");
                        //model.getCardState();
                    }
                    break;
                default:
                    System.out.println("Error in processing card choice");
            }
            cardState = model.getCardState();
        }
    }
}
