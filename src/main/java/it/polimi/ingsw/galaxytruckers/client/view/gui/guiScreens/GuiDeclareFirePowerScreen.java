package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.state.DeclareFirePowerState;

/**
 * GUI screen for the firepower declaration phase.
 * This screen allows players to activate double-cannons with batteries
 * to increase their ship's firepower.
 */
public class GuiDeclareFirePowerScreen extends GuiActivationScreen {
    /**
     * Constructs a new firepower declaration screen.
     *
     * @param model                The client model containing game state
     * @param controller           The controller for communicating with the server
     * @param declareFirePowerState The state containing valid actions for this phase
     */
    public GuiDeclareFirePowerScreen(ClientModel model, ClientControllerInterface controller, DeclareFirePowerState declareFirePowerState) {
        super(model, controller, declareFirePowerState);
        if (isMyTurn()) {
            guiLog.log("Select an double-cannon to activate or a battery to use");
        }
    }
}
