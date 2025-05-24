package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

public class CliRemoveCrewScreen extends CliScreen {
    public CliRemoveCrewScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void render() {

    }

    @Override
    public void parseAndInvoke(String input) {

    }
}
