package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GrabRewardState;

/**
 * CLI screen for handling the reward phase in the Galaxy Truckers game.
 */
public class CliRewardScreen extends CliAdventureScreen {

    /**
     * Creates a new reward screen with the given game state.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState The current grab-reward state
     */
    public CliRewardScreen(ClientModel model, ControllerToServer controller, GrabRewardState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);
        printCurrentCard().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        if (isMyTurn) {
            System.out.println("Your turn to decide whether to take the reward");
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to decide about the reward");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        if (input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }
        if (!isMyTurn) {
            System.out.println("It's not your turn to grab a reward");
            return;
        }
        if(input.equalsIgnoreCase("P")) controller.grabReward();
        if(input.equalsIgnoreCase("")) controller.goNext();
    }

//    @Override
//    public void notifyGrabReward(ShipBoard shipBoard, boolean grabbed) {
//        CliShipBoard ship = shipToCliShip.get(shipBoard);
//        cliAllShips.setDirty();
//        cliFlightBoard.setDirty();
//    }
}
