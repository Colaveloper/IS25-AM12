package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.ActivateState;

public class CliActivationScreen extends CliScreen {
    public CliActivationScreen(ClientModel model, ControllerToServer controller, ActivateState activateState) {
        super(model, controller, activateState);
    }

    @Override
    public void render() {

    }

    @Override
    public void parseAndInvoke(String input) {

    }
}
