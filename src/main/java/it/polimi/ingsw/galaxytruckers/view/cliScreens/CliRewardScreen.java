package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GrabRewardState;

public class CliRewardScreen extends CliScreen {

    private final boolean isMyTurn;
    private final ShipBoard currentShip;

    public CliRewardScreen(ClientModel model, ControllerToServer controller, GrabRewardState gameState) {
        super(model, controller, gameState);
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
    }

    @Override
    public boolean isInputLegal(String input) {
        if (!isMyTurn) {
            System.out.println("It's not your turn to grab a reward");
            return false;
        }

        // any input other than ENTER is considered a grab action
        return true;
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if (isMyTurn) {
            System.out.println("Your turn to decide whether to take the reward");
            System.out.println("Press ENTER to SKIP the reward or any other key to grab it");
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to decide about the reward");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        if (!isMyTurn) {
            System.out.println("It's not your turn to grab a reward");
            return;
        }

        if (input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }

        controller.grabReward(!input.isEmpty());
    }

    @Override
    public void notifyGrabReward(ShipBoard shipBoard, boolean grabbed) {
        CliShipBoard ship = shipToCliShip.get(shipBoard);
        cliAllShips.setDirty();
        cliFlightBoard.setDirty();
    }
}
