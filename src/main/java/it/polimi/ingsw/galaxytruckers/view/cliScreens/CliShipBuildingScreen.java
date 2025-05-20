package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliShipBuildingScreen extends CliScreen {
    CliComponentBank componentBank;
    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliShipBuildingScreen(ClientModel model, ClientController controller) throws IOException {
        super(model, controller);

        componentBank = CliComponentBank.getInstance(model);
        componentBank.addListener(this);

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();

        output.addAll(componentBank.getDescription());
        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());

        output.add("C       \tGet New covered component" + "\t\t\tU [i]   \tGet i-th uncovered component");
        //output.add("U [i]   \tGet i-th uncovered component");
        output.add("S [i]   \tGet i-th stashed component" + "\t\t\tF [i]   \tGet i-th forecast deck");         // NOT IN Levels.TEST
        //output.add("F [i]   \tGet i-th forecast deck");             // NOT IN Levels.TEST

        if (model.existsUnwelded()) {
            //output.add("R       \tReject current component");
            output.add("P [x] [y] \tPlace last component in x, y" + "\t\t\tR       \tReject last component");
            output.add("S       \tStash last component");        // NOT IN Levels.TEST
            output.add("L       \tRotate last component left");
        }
        else if (model.currentComponentProperty().get().getType() != ComponentType.EMPTY_AREA) {
            //output.add("R       \tReject current component");
            output.add("P [x] [y] \tPlace current component in x, y" + "\t\t\tR       \tReject current component");
            output.add("S       \tStash current component");        // NOT IN Levels.TEST
            output.add("L       \tRotate current component left");
        }

        output.add("H       \tFlip hourglass");                     // NOT IN Levels.TEST

        return output;
    }

    @Override
    public boolean isLegalInput(String input) {
        return input.matches("^(C|U \\d+|S(?: \\d+)?|F \\d+|R|P \\d+ \\d+|L|H)$");
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
                    int index = Integer.parseInt(parts[1])-1;
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

            default:
                // should be impossible
                System.out.println("Invalid command.");
                break;
        }
    }
}