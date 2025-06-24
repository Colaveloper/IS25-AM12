package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareFirePowerState;
import javafx.scene.control.Label;

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
    public GuiDeclareFirePowerScreen(ClientModel model, ControllerToServer controller, DeclareFirePowerState declareFirePowerState) {
        super(model, controller, declareFirePowerState);
        if (isMyTurn()) {
            guiLog.log("Select an double-cannon to activate or a battery to use");
        }
    }
}
