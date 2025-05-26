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

    private boolean shipNotValid;

    public CliValidationScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);
        shipNotValid = gameState.getValidShipBoards().contains(model.getMyShip());
    }

    @Override
    public void render() {
        printShips();

        shipNotValid = gameState.getValidShipBoards().contains(model.getMyShip());
        if (shipNotValid) {
            System.out.println("your ship is invalid, choose a component to remove");
        }
        else{
            System.out.println("someone else has an invalid ship, wait while they correct them");
        }

        printActions();
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
        if(shipNotValid) {
            String[] parts = input.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            Point inputPoint = new Point(x, y);
            controller.removeComponent(inputPoint);
        }
    }


}
