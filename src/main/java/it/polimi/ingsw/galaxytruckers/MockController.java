package it.polimi.ingsw.galaxytruckers;/*
* MAIN CLASS SIMULATING THE CONTROLLER CALLING MODEL METHODS
* THIS IS FOR TESTING PURPOSES ONLY
* */
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MockController {
    public static void main(String[] args){
        Scanner userScanner = new Scanner(System.in);
        String input;
        boolean submit = false;

        // PLAYING STEPS
        // 1. Host starts the game session picking a game-level
        // 2. Guests pick a nickname and join the session  // TODO: implement nicknames
        // 3. Everybody concurrently picks a color
        // 4. Everybody claims to be ready (easier if cannot be taken back)
        // 5. GameModel instantiation // TODO: instantiate cards and components from JSON
        // 6. ShipBuilding // TODO: choose how to distinguish between levels in hourglass usage, peeking
        // 7. Everybody claims to be ready or time is up
        // 8. Flight


        // Mocking 1.
        Level chosenLevel = Level.SECOND;

        // Assuming 2.

        // Mocking 3.
        Map<Integer, Colors> chosenColors = new HashMap<>();
        chosenColors.put(1234, Colors.BLUE);
        chosenColors.put(2345, Colors.RED);
        chosenColors.put(3456, Colors.GREEN);

        // Assuming 4.

        // 5. (minus the JSON)
        GameModel gameModel = new GameModel(chosenLevel, chosenColors);

        // Mocking 6.
        BuildingTime buildingTime = new BuildingTime(chosenLevel.getHourglassFlips());
        gameModel.requestRanComponent(1234);
        gameModel.rotateComponent(1234);
        gameModel.placeComponent(1234, new Point(7,8));
        gameModel.placeShipOnFlightBoard(1234, chosenLevel.getStartingPositions().get(2));

        // Assuming 7.

        // Mocking 8.
        gameModel.drawCard();
        Query<Integer, PlayerAction> query = gameModel.getNextQueryToPlayer();
        Integer queriedPlayer = query.playerID();
        PlayerAction playerAction = query.expectedAction();

        int projectileIndex = 0; // TODO: try to do without

//        simulating drawing the AbandonedShip card
//        List<CardState> activeCardState = gameModel.getCardStates();
//        System.out.println("The " + gameModel.getCardName() + " card has been drawn\nResident sacrifice: " +
//                gameModel.getCardSacrifice() + "\nCredit gains: " + gameModel.getCardCredits()  +
//                "\nFlight days cost: " + gameModel.getCardFlightDaysLost());

        System.out.println("The " + gameModel.getCardName() + " card has been drawn!");
        while(playerAction != PlayerAction.END_CARD) {
            switch (playerAction) {
                case ASK_IF_PASS:
//                    if (model.getCurrentPlayerIndex() < 4) {
                        //this is simulating the controller passing this onto the view,
                        //which will ask for user input
                        System.out.println("Do you wish to pass this card to the next player?(y/n): ");
                        input = userScanner.nextLine();

                        if (input.equals("y")) {
                            //gameModel.resetSteps();
                            gameModel.passCardToNextPlayer();
                            break;
                        } else if (input.equals("n")) {
                            //continue with the switch case
                        } else {
                            System.out.println("Invalid input.");
                            System.out.println("Do you wish to pass this card to the next player?(y/n): ");
                            input = userScanner.nextLine();
                        }
//                    }
                    break;
                case LOSE_RESIDENTS:
                    gameModel.loseResidents(gameModel.getCardSacrifice());//makes the current player lose residents
                    break;
                case ACTIVATE_CANNONS:
                    //user input
                    submit = false;
                    while (!submit) {
                        System.out.println("Do you want to activate a CANNON to increase power? (y/n):");
                        input = userScanner.nextLine();
                        if (input.equals("y")) {
                            // get position from input
                            gameModel.activateComponent(123, new Point(3, 4));
                        } else if (input.equals("n")) {
                            submit = true;
                            //just continues onto next step
                            //gameModel.getCardStates();
                        } else {
                            System.out.println("Invalid input. Going ahead to next step");
                            //gameModel.getCardState();
                        }
                    }
                    break;
                case ROLL_DICE:
                    //user input
                    System.out.println("Press ENTER to roll the dice");
                    input = userScanner.nextLine();
                    gameModel.setRollDice();
                    projectileIndex += 1;
                    //roll = gameModel.rollDice();
                case ACTIVATE_SHIELDS:

                    System.out.println("Do you want to activate a SHIELD? (y/n):");
                    input = userScanner.nextLine();

                    if (input.equals("y")) {
                        // print available shields that would defend the player
//                        System.out.println("What shield to activate (int, int):");
//                        input = userScanner.nextLine();
                        gameModel.activateComponent(123, new Point(3, 4));

                    } else if (input.equals("n")) {
                        gameModel.fireCannonAtPlayer(projectileIndex);
                    } else {
                        System.out.println("Invalid input. Going ahead to next step");
                    }
                    break;
                case MANAGE_GOODS:
                    gameModel.grabGoods(gameModel.getCardGoods());
                    break;
                case LOSE_GOODS:
                    //TODO: potentially remove this from controller
                    //it may be completely automatic
                    break;
                case START_CARD:
                    System.out.println("start card state");
                    break;
//                case GET_BLASTED:
//                    System.out.println("get blasted state");
//                    break;
//                case SUBMIT_POWER:
//                    submit = false;
//                    System.out.println("submit state");
//                    break;
                case CHOOSE_PLANET:
                    int numPlanetInput;
                    //TODO: fix this to account for number of planets available
                    System.out.println("Choose planet (0,1,2)");
                    numPlanetInput = userScanner.nextInt();
                    if(numPlanetInput != 0 && numPlanetInput != 1 && numPlanetInput != 2){
                        System.out.println("Invalid planet to land on. Going to next player");
                        //gameModel.passCardToNextPlayer();
                    }
                    else{
                        gameModel.landOnPlanet(numPlanetInput);
                    }
                    break;
                case ACTIVATE_ENGINES:
                    //user input
                    System.out.println("Do you want to activate an ENGINE to increase power? (y/n):");
                    input = userScanner.nextLine();
                    if (input.equals("y")) {
                        gameModel.activateComponent(123, new Point(3, 4));
                    } else if (input.equals("n")) {
                        //just continues onto next step
                        //gameModel.getCardStates();
                    } else {
                        System.out.println("Invalid input. Going ahead to next step");
                        //gameModel.getCardState();
                    }
                    break;
                default:
                    System.out.println("Error in processing card choice");
            }
            playerAction = gameModel.getNextQueryToPlayer().expectedAction();
        }
    }
}
