package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.enums.ComponentType;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliPointSelectionScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliPointSelectionScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);
    }



    @Override
    protected List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();
        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());
        if(model.isMyTurn()) {
            output.add("select component to activate");
        }
        else {
            output.add("it's not your turn");
        }

        return output;
    }



    @Override
    public boolean isLegalInput(String input) {
        // check if input = number + space + number
        if (!input.matches("\\d+ \\d+")) return false;

        // check if the point made from those numbers is selectable
        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        Point p = new Point(x, y);
        return model.getSelectablePoints().contains(p);
    }

    @Override
    public void parseAndInvoke(String input) {
        if(model.isMyTurn()) {
            String[] parts = input.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            Point p = new Point(x, y);
            controller.activateComponent(p);
        }
    }


}
