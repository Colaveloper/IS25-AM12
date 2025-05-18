package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CliJoinOrCreateScreen extends CliScreen {
    public CliJoinOrCreateScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public boolean isLegalInput(String input) {
        if (input == null) return false;
        if (input.equalsIgnoreCase("C")) return true;

        try {
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }    }

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
    protected List<String> getNewDescription() throws IOException {
        return List.of("Input the UUID of an existing lobby, or C to create a new one");
    }
}
