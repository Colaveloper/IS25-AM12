package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GrabRewardState;

public class CliRewardScreen extends CliAdventureScreen {


    public CliRewardScreen(ClientModel model, ControllerToServer controller, GrabRewardState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);
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
        controller.grabReward(input.equalsIgnoreCase("P"));//todo doesn t work
    }

//    @Override
//    public void notifyGrabReward(ShipBoard shipBoard, boolean grabbed) {
//        CliShipBoard ship = shipToCliShip.get(shipBoard);
//        cliAllShips.setDirty();
//        cliFlightBoard.setDirty();
//    }
}
