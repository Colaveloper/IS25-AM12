package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
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

public class NicknameChoiceScreen extends ScreenStrategy {

    public NicknameChoiceScreen(ClientModel model) {
        super(model);
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
        server.registerNickname(input);
    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) {
        root.getChildren().clear();

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        root.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label prompt = new Label("Please choose a unique nickname to proceed:");
        TextField nicknameField = new TextField();
        nicknameField.setMaxWidth(200);
        Button submitButton = new Button("Submit");

        nicknameField.setOnAction(e -> submitNickname(nicknameField, server));
        submitButton.setOnAction(e -> submitNickname(nicknameField, server));

        layout.getChildren().addAll(prompt, nicknameField, submitButton);
        Platform.runLater(nicknameField::requestFocus);
        root.getChildren().add(layout);
    }

    private void submitNickname(TextField field, VirtualServer server) {
        String nickname = field.getText().trim();
        if (!nickname.isEmpty()) {
            try {
                server.registerNickname(nickname);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
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
