package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.Player;

/**
 * CLI screen for displaying the lobby in the Galaxy Truckers game.
 */
public class CliLobbyScreen extends CliScreen {

    /**
     * Creates a new lobby screen with the given model and controller.
     * This screen displays the players currently in the lobby.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     */
    public CliLobbyScreen(ClientModel model, ClientControllerInterface controller) {
        super(model, controller);
    }

    @Override
    public void parseAndInvoke(String input) {}

    @Override
    public boolean isInputLegal(String input) {
        return false;
    }

    @Override
    public void render() {
        StringBuilder lobbyScreen = new StringBuilder("Currently in lobby: ");
        for(Player player : model.getPlayers()) {
            lobbyScreen.append(" ").append(player.getNickname());
        }
        System.out.println(lobbyScreen);
    }
}
