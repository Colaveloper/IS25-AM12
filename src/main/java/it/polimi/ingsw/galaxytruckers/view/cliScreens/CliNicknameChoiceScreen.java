package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
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

    public CliNicknameChoiceScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public boolean isLegalInput(String input) {
        return true;
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
    public List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();

        output.add("Successfully bound to the server ✅");
        output.add("Please choose a unique nickname in order to proceed: ");

        return output;
    }
}
