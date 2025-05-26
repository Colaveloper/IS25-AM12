package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;

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
        printShips();

        if(model.getMyShip().equals(gameState.getShipBoard())) {
            System.out.println("select component to activate");
        }
        else {
            System.out.println("it's not your turn");
        }
        printActions();
    }


    @Override
    public void parseAndInvoke(String input) {
        if(model.getMyShip().equals(gameState.getShipBoard())) {
            Point p = getPoint(input);
            if(model.getMyShip().getBatteries().containsKey(p)){
                controller.useBattery(p);
            }
            else {
                controller.activateComponent(p);
            }
        }
    }
}
