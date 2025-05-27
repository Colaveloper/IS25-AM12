package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.TestShipBuildingState;

public class CliTestShipBuildingScreen extends CliScreen {

    private CliComponentBank componentBank;
    private TestShipBuildingState gameState;

    public CliTestShipBuildingScreen(ClientModel model, ControllerToServer controller, TestShipBuildingState gameState) {
        super(model, controller, gameState);
        this.componentBank = new CliComponentBank(model);
        this.gameState = gameState;
    }

    @Override
    public void render() {
        System.out.println(componentBank.getNewDescription());
        printShips();
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s+");
        switch (parts[0]) {
            case "C":
                controller.requestRandComponent();
                break;

            case "U":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]) - 1;
                    int componentId = gameState.getComponentBank().getUncoveredComponents().get(index).getId();
                    controller.requestComponent(componentId);
                }
                break;

            case "S":
                if (parts.length == 1) {
                    controller.stashComponent();
                } else if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.grabStashedComponent(index);
                }
                break;

            case "F":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.acquireForecast(index);
                }
                break;

            case "R":
                controller.rejectComponent();
                break;

            case "P":
                if (parts.length == 3) {
                    controller.placeComponent(getPoint(input), 0); //todo add orientation
                }
                break;

            case "L":
                //model.rotateCurrentComponentLeft();
                //todo rotate component in model
                break;

            case "H":
                controller.flipHourglass();
                break;

            case "E":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.placeShipOnFlightboard(index);
                }
                break;

            default:
                // should be impossible
                System.out.println("Invalid command.");
                break;
        }
    }

    @Override
    public boolean isInputLegal(String input) {
        if (!isFormatLegal(input)) return false;

        if (input.matches("^[A-Za-z]\\d\\d$")) {


        }
        return true;
    }
}
