package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CliPointSelectionScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliPointSelectionScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);

        flightBoard = new CliFlightBoard(model);
        allShips = new CliAllShips(model.getShipToPlayer());
    }



    @Override
    public void render() {
        List<String> output = new ArrayList<>();
        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());
        if(model.isMyTurn()) {
            output.add("select component to activate");
        }
        else {
            output.add("it's not your turn");
        }
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
