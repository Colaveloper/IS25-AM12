package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareEnginePowerState;

/**
 * CLI screen for declaring engine power in the Galaxy Truckers game.
 */
public class CliDeclareEnginePowerScreen extends CliActivationScreen {

    private final DeclareEnginePowerState gameState;
    private final boolean isMyTurn;
    private final ShipBoard currentShip;

    /**
     * Creates a new declare engine power screen with the given game state.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState The current declare engine power state
     */
    public CliDeclareEnginePowerScreen(ClientModel model, ControllerToServer controller, DeclareEnginePowerState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);
        printCurrentCard().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        if (isMyTurn) {
            System.out.println("Your turn to declare engine power");
            System.out.println("Select engine components to activate or batteries to use");
            System.out.println("Available engines: " + gameState.getAvailablePositions().size());
            System.out.println("Available batteries: " + currentShip.getBatteries().size());
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to declare engine power");
        }
        printActions();
    }
}
