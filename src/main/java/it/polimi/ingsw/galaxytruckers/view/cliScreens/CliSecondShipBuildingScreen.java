package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;

import java.awt.*;

public class CliSecondShipBuildingScreen extends CliScreen {

    CliComponentBank componentBank;
    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState gameState) {
        super(model, controller, gameState);

        this.componentBank = new CliComponentBank(model);
        this.flightBoard = new CliFlightBoard(model);
        this.allShips = new CliAllShips(model);
    }

    @Override
    public void render() {

        System.out.println(componentBank.getDescription());
        System.out.println(flightBoard.getDescription());
        System.out.println(allShips.getDescription());

        System.out.println("C       \tGet New covered component" + "\t\t\tU [i]   \tGet i-th uncovered component");
        System.out.println(model.getClientPlayer().getShipBoard().getStashedComponents().isEmpty() ? "S [i]   \tGet i-th stashed component" : "");
        System.out.println("F [i]   \tGet i-th forecast deck");

        if (model.getClientPlayer().getShipBoard().getLastComponent().isEmpty()) {
            System.out.println("P [x] [y] \tPlace unwelded component in x, y" + "\t\t\tR       \tReject unwelded component");
            System.out.println("S       \tStash unwelded component");
            System.out.println("L       \tRotate unwelded component left");
        }

        System.out.println("H       \tFlip hourglass");
        System.out.println("E [i]   \tend and place on flightboard");
    }

//    @Override
//    public boolean isLegalInput(String input) {
//        return input.matches("^(C|U \\d+|S(?: \\d+)?|F \\d+|R|P \\d+ \\d+|L|H|E \\d+)$");
//    }

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
                    int componentId = model.revealedComponentsProperty().get(index).getComponentId();
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
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    controller.placeComponent(new Point(x, y), 0);//todo add orientation
                }
                break;

            case "L":
                model.rotateCurrentComponentLeft();
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
}
