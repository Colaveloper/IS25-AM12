package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ConfigFactory;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliShipBuildingScreen extends CliScreen {
    ConfigFactory config;
    CliComponentBank componentBank;
    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliShipBuildingScreen(ClientModel model, ClientController controller, ConfigFactory config) throws IOException {
        super(model, controller);
        this.config = config;

        componentBank = CliComponentBank.getInstance(model);
        componentBank.addListener(this);

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model, config);
        allShips.addListener(this);
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();

        output.addAll(componentBank.getDescription());
        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());

        output.add("C       \tGet New covered component" + "\t\t\tU [i]   \tGet i-th uncovered component");
        output.add(config.isStashingAllowed() ? "S [i]   \tGet i-th stashed component" : "");
        output.add(config.isForecastPresent() ? "F [i]   \tGet i-th forecast deck" : "");

        if (model.getExistsUnweldedComponent()) {
            output.add("P [x] [y] \tPlace unwelded component in x, y" + "\t\t\tR       \tReject unwelded component");
            output.add(config.isStashingAllowed() ? "S       \tStash unwelded component" : "");
            output.add("L       \tRotate unwelded component left");
        }

        output.add(config.isHourglassPresent() ? "H       \tFlip hourglass" : "");

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