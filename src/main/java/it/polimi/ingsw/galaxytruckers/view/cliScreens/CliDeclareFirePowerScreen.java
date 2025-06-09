package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareFirePowerState;

import java.awt.*;
import java.util.Set;

public class CliDeclareFirePowerScreen extends CliActivationScreen {

    private final DeclareFirePowerState gameState;
    private final boolean isMyTurn;
    private final boolean imOut;
    private final ShipBoard currentShip;

    public CliDeclareFirePowerScreen(ClientModel model, ControllerToServer controller, DeclareFirePowerState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(myShipBoard);
        imOut = gameState.getImOut();
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        if (isMyTurn) {
            System.out.println("Your turn to declare fire power");
            System.out.println("Select cannon components to activate or batteries to use");
            System.out.println("Available cannons: " + gameState.getAvailablePositions().size());
            System.out.println("Available batteries: " + currentShip.getBatteries().size());
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to declare fire power");
        }

        printActions();
    }
}
