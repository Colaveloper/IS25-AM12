package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.state.ActivateState;

/**
 * GUI screen for the engine power declaration phase.
 * This screen allows players to activate double-engines with batteries
 * to power their ship during the flight phase of the game.
 */
public class GuiDeclareEnginePowerScreen extends GuiActivationScreen {
    /**
     * Constructs a new engine power declaration screen.
     *
     * @param model      The client model containing game state
     * @param controller The controller for communicating with the server
     * @param state      The activation state containing valid actions for this phase
     */
    public GuiDeclareEnginePowerScreen(ClientModel model, ClientControllerInterface controller, ActivateState state) {
        super(model, controller, state);
        if (isMyTurn()) {
            guiLog.log("Select an double-engine to activate or a battery to use");
        }
    }
}
