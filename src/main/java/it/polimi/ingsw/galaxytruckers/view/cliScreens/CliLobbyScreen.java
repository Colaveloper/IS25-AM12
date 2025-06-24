package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

import java.io.IOException;
import java.util.List;

public class CliLobbyScreen extends CliScreen {

    /**
     * Creates a new lobby screen with the given model and controller.
     * This screen displays the players currently in the lobby.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     */
    public CliLobbyScreen(ClientModel model, ControllerToServer controller) {
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
