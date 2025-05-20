package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.screens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GuiNicknameChoiceScreen extends GuiScreen {
    public GuiNicknameChoiceScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public void attachContentToRoot(VBox root) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        root.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label prompt = new Label("Please choose a unique nickname to proceed:");
        TextField nicknameField = new TextField();
        nicknameField.setMaxWidth(200);
        Button submitButton = new Button("Submit");

        nicknameField.setOnAction(e -> submitNickname(nicknameField));
        submitButton.setOnAction(e -> submitNickname(nicknameField));

        layout.getChildren().addAll(prompt, nicknameField, submitButton);
        Platform.runLater(nicknameField::requestFocus);
        root.getChildren().add(layout);
    }

    private void submitNickname(TextField field) {
        String nickname = field.getText().trim();
        if (!nickname.isEmpty()) {
            try {
                controller.registerNickname(nickname);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
