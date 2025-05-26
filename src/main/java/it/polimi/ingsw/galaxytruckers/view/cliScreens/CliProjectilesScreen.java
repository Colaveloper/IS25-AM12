package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;

public class CliProjectilesScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;
    String direction;


    public CliProjectilesScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);

        flightBoard = new CliFlightBoard(model);
        allShips = new CliAllShips(model.getShipToPlayer());
    }

    @Override
    public void render() {
        printShips();
        printActions();

        switch (gameState.getProjectile().direction()) {
            case 0:
                direction = "front on column";
                break;
            case 1:
                direction = "right or left on row";//todo
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

        System.out.println("a" + gameState.getProjectile().type() +
                "is approaching from" + direction + gameState.getProjectile().roll());
        if(model.getMyShip().equals(gameState.getShipBoard())) {
            if (!gameState.getAvailablePositions().isEmpty()) {
                System.out.println("choose what to activate OR a battery to use");
            }
            System.out.println("S to submit end activations");
        }
        else {
            System.out.println("it's not your turn");
        }
    }

//
//    @Override
//    public boolean isLegalInput(String input) {
//        // check if input = number + space + number
//        if (!input.matches("\\d+ \\d+") || !input.matches("C")) return false;
//
//        // check if the point made from those numbers is selectable
//        String[] parts = input.split(" ");
//        int x = Integer.parseInt(parts[0]);
//        int y = Integer.parseInt(parts[1]);
//        Point p = new Point(x, y);
//        return model.getSelectablePoints().contains(p) && model.getSelectableBatteries().contains(p);
//    }

    @Override
    public void parseAndInvoke(String input) {
        if(model.getMyShip().equals(gameState.getShipBoard())) {
            String[] parts = input.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            Point p = new Point(x, y);
//            Component component = model.getComponent(model.getMyNickname(), p);
//            if (component.getType() == ComponentType.BATTERY) {
//                controller.useBattery(p);
//            } else {
//                controller.activateComponent(p);
//            }
        }
    }
}
