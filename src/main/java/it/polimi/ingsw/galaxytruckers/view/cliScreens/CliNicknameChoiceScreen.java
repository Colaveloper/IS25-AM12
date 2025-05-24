package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliNicknameChoiceScreen extends CliScreen {

    public CliNicknameChoiceScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);
    }


    @Override
    public void parseAndInvoke(String input) {
        try {
            controller.registerNickname(input);
            controller.setMyNickname(input);
        } catch (IllegalArgumentException e) {
            controller.reportError(e.getMessage());
        }

    }

    @Override
    public void render() {
        System.out.println("Successfully bound to the server ✅");
        System.out.println("Please choose a unique nickname in order to proceed: ");
    }
}
