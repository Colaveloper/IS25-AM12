package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareEnginePowerState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;

public class CliPointSelectionScreen extends CliScreen {

    public CliPointSelectionScreen(ClientModel model, ControllerToServer controller, DeclareEnginePowerState gameState) {
        super(model, controller, gameState);
    }


    @Override
    public boolean isInputLegal(String input) {
        if (!isFormatLegal(input)){
            return false;
        }
        Point p = getPoint(input);

        return gameState.getAvailablePositions().contains(p);
    }

    @Override
    public void render() {
        allShips.highlightPoints(model.getShipToPlayer().get(gameState.getShipBoard()), gameState.getAvailablePositions(), Highlights.GREEN);

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
