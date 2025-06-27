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

/**
 * ClIScreen for choosing a nickname in the CLI version of Galaxy Truckers.
 */
public class CliNicknameChoiceScreen extends CliScreen {

    /**
     * Creates a new nickname choice screen with the given model and controller.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     */
    public CliNicknameChoiceScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    @Override
    public void parseAndInvoke(String input) {
        controller.registerNickname(input);
    }

    @Override
    public boolean isInputLegal(String input) {
        return !input.trim().isEmpty();
    }

    @Override
    public void render() {
        System.out.println("Successfully bound to the server ✅");
        System.out.println("Please choose a unique nickname in order to proceed: ");
    }
}
