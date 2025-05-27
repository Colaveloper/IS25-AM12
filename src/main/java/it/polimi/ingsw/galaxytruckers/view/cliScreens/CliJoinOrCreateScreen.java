package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CliJoinOrCreateScreen extends CliScreen {
    public CliJoinOrCreateScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    @Override
    public boolean isInputLegal(String input) {
        if (input.equalsIgnoreCase("C")) return true;
        try {
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void parseAndInvoke(String input) {
        if (input.equalsIgnoreCase("C")) {
            controller.showGameCreation();
        } else {
            System.out.println("trying to join");
            controller.joinLobby(UUID.fromString(input));
        }
    }

    @Override
    public void render() {
        System.out.println("Input the UUID of an existing lobby, or C to create a new one");
    }
}
