package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Lobby;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * CLI screen for joining or creating a game lobby.
 */
public class CliJoinOrCreateScreen extends CliScreen {
    private final List<UUID> ids;

    /**
     * Creates a new join-or-create-screen with the given model and controller.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     */
    public CliJoinOrCreateScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
        ids = new ArrayList<>();
        ids.addAll(model.getActiveLobbies().keySet());
    }

    @Override
    public boolean isInputLegal(String input) {
        if (input.isEmpty()) return false;
        if (input.equalsIgnoreCase("C")) return true;
        if (input.matches("\\d*") && Integer.parseInt(input) < ids.size()) return true;
        return false;
    }

    @Override
    public void parseAndInvoke(String input) {
        if (input.equalsIgnoreCase("C")) {
            controller.showGameCreation();
        } else {
            System.out.println("trying to join");
            controller.joinLobby(ids.get(Integer.parseInt(input)));
        }
    }

    @Override
    public void render() {
        if(!ids.isEmpty()) System.out.println("Input the index of an existing lobby, or C to create a new one");
        else System.out.println("C to create a new lobby");

        for (UUID id : ids) {
            Lobby lobby = model.getActiveLobbies().get(id);
            System.out.println(
                    ids.indexOf(id) +
                    " - host: " + lobby.getHost() +
                    "\tLevel: " + lobby.getLevel()
            );
        }
    }

    @Override
    public void notifyNewLobby(Lobby lobby) {
        ids.add(lobby.getId());
    }

    @Override
    public void notifyRemoveLobby(UUID LobbyId) {
        ids.remove(LobbyId);
    }
}
