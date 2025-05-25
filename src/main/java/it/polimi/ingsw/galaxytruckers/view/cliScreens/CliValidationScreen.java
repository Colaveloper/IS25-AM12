package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CliValidationScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliValidationScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);

        flightBoard = new CliFlightBoard(model);
        allShips = new CliAllShips(model.getShipToPlayer());
    }

//    @Override
//    public boolean isLegalInput(String input) {
//        // Validate format using regex
//        if (!input.matches("\\d+ \\d+")) {
//            return false;
//        }
//
//        // Split input and parse numbers
//        String[] parts = input.split(" ");
//        int x = Integer.parseInt(parts[0]);
//        int y = Integer.parseInt(parts[1]);
//
//        if( x < model.getUpLeft().x || y < model.getUpLeft().y ||
//        x > model.getBottomRight().x || y > model.getBottomRight().y) {
//            return false;
//        }
//
//        // Create point and check list
//        Point inputPoint = new Point(x, y);
//        ComponentType type = model.getComponent(model.getMyNickname(), inputPoint).getType();
//        return type != ComponentType.EMPTY_AREA && type != ComponentType.EMPTY_SPACE;
//    }

    @Override
    public void parseAndInvoke(String input)  {
        if(!model.shipIsValid()) {
            String[] parts = input.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            Point inputPoint = new Point(x, y);
            controller.removeComponent(inputPoint);
        }
    }

    @Override
    public void render() {
        List<String> output = new ArrayList<>();

        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());

        if (!model.shipIsValid()) {
            output.add("your ship is invalid, choose a component to remove");
        }
        else{
            output.add("someone else has an invalid ship, wait while they correct them");
        }
    }
}
