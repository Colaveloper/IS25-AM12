package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
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

    public CliLobbyScreen(ClientModel model, ClientController controller, GameState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void parseAndInvoke(String input) {}

    @Override
    public void render() {
        System.out.print("Currently in lobby: ");
        for(Player player : model.getPlayers()) {
            System.out.print(player.getNickname());
        }
    }
}
