package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.Set;

public class CliProjectilesScreen extends CliScreen {

    public CliProjectilesScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);


    }

    @Override
    public void render() {
        Player player = model.getShipToPlayer().get(gameState.getShipBoard());
        allShips.highlightPoints(player, gameState.getAvailablePositions(), Highlights.RED);                    //color activatable red
        allShips.highlightPoints(player, gameState.getShipBoard().getBatteries().keySet(), Highlights.GREEN);   //color batteries green

        printShips();

        String direction = switch (gameState.getProjectile().direction()) {
            case 0 -> "front on column";
            case 1 -> "right or left on row";//todo
            case 2 -> "back on column";
            case 3 -> "right or left on row";
            default -> "error";
        };

        System.out.println("a" + gameState.getProjectile().type() +
                "is approaching from" + direction + gameState.getProjectile().roll());

        if(model.getMyShip().equals(gameState.getShipBoard())) {
            System.out.println("select component to activate");
        }
        else {
            System.out.println("it's not your turn");
        }

        printActions();
    }

//
//    @Override
//    public boolean isFormatLegal(String input) {
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
        if(input.equalsIgnoreCase("X")){
            controller.giveUp();
        }
        if (model.getMyShip().equals(gameState.getShipBoard())) {
            Point p = getPoint(input);
            if (model.getMyShip().getBatteries().containsKey(p)) {
                controller.useBattery(p);
            }
            else {
                controller.activateComponent(p);
            }
        }
    }

    @Override
    public boolean isInputLegal(String input) {
        if (!isFormatLegal(input)) return false;
        Point p = getPoint(input);
        return
                model.getMyShip().getActivatables().containsKey(p) ||
                model.getMyShip().getBatteries().containsKey(p);
    }
}
