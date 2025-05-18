package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
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

    public CliLobbyScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public boolean isLegalInput(String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(String input) {}

    @Override
    public List<String> getNewDescription() throws IOException {
        return List.of("Currently in lobby: "+model.getNicknames());
    }
}
