package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareFirePowerState;

/**
 * CLI screen for declaring fire power in the Galaxy Truckers game.
 */
public class CliDeclareFirePowerScreen extends CliActivationScreen {

    private final DeclareFirePowerState gameState;
    private final boolean isMyTurn;
    private final ShipBoard currentShip;

    /**
     * Creates a new declare firepower screen with the given game state.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState The current declare-firepower state
     */
    public CliDeclareFirePowerScreen(ClientModel model, ControllerToServer controller, DeclareFirePowerState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(myShipBoard);
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
            System.out.println("Your turn to declare fire power");
            System.out.println("Select cannon components to activate or batteries to use");
//            System.out.println("Available cannons: " + activateablesComp);
//            System.out.println("Available batteries: " + numBatteriesComp);
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to declare fire power");
        }

        printActions();
    }
}
