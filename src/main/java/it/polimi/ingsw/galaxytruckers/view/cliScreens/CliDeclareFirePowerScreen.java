package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareFirePowerState;

import java.awt.*;

public class CliDeclareFirePowerScreen extends CliScreen {

    private DeclareFirePowerState gamestate;

    public CliDeclareFirePowerScreen(ClientModel model, ControllerToServer controller, DeclareFirePowerState gameState) {
        super(model, controller, gameState);
        this.gamestate = gameState;
    }


    @Override
    public boolean isInputLegal(String input) {
        if (!isFormatLegal(input)){
            return false;
        }
        Point p = getPoint(input);

        return gamestate.getAvailablePositions().contains(p);
    }

    @Override
    public void render() {
//        allShips.highlightPoints(model.getShipToPlayer().get(gamestate.getShipBoard()), gamestate.getAvailablePositions(), Highlights.GREEN);

        printShips();

        if(model.getMyShip().equals(gamestate.getShipBoard())) {
            System.out.println("select component to activate");
        }
        else {
            System.out.println("it's not your turn");
        }
        printActions();
    }


    @Override
    public void parseAndInvoke(String input) {
        if(model.getMyShip().equals(gamestate.getShipBoard())) {
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
