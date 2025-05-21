package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliProjectilesScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;
    String direction;


    public CliProjectilesScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();
        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());

        switch (model.getCurrentProjectile().direction()) {
            case 0:
                direction = "front on column";
                break;
            case 1:
                direction = "right or left on row";
                break;
            case 2:
                direction = "back on column";
                break;
            case 3:
                direction = "right or left on row";
                break;
            default:
                direction = "error";
        }

        output.add("a" + model.getCurrentProjectile().type().name() +
                "is approaching from" + direction + model.getCurrentProjectile().roll());
        if (!model.getSelectablePoints().isEmpty()) {
            output.add("choose what to activate AND a battery to use");
        }
        return output;
    }


    @Override
    public boolean isLegalInput(String input) {
        // check if input = number + space + number
        if (!input.matches("\\d+ \\d+ \\d+ \\d+")) return false;

        // check if the point made from those numbers is selectable
        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        Point p = new Point(x, y);
        x = Integer.parseInt(parts[2]);
        y = Integer.parseInt(parts[3]);
        Point battery = new Point(x, y);
        return model.getSelectablePoints().contains(p) && model.getSelectableBatteries().contains(battery);
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        Point p = new Point(x, y);
        controller.activateComponent(p);

        //todo: battery è attivabile?
        x = Integer.parseInt(parts[2]);
        y = Integer.parseInt(parts[3]);
        Point battery = new Point(x, y);
        controller.useBattery(battery);
    }
}
