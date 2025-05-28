package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GuiNicknameChoiceScreen extends GuiScreen {
    public GuiNicknameChoiceScreen(ClientModel model, ClientController controller, GameState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void attachContentToRoot(VBox root) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setMaxWidth(400);
        layout.setStyle(
                "-fx-background-color: rgba(0, 0, 50, 0.4); -fx-background-radius: 20;"
        );

        Label prompt = new Label("Please choose a unique nickname to proceed:");
        prompt.setFont(new Font("Arial", 20));
        prompt.setTextFill(Color.LIGHTSKYBLUE);
        prompt.setWrapText(true);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setEffect(new DropShadow(8, Color.DARKSLATEBLUE));
        prompt.setMaxWidth(350);

        TextField nicknameField = new TextField();
        nicknameField.setMaxWidth(Double.MAX_VALUE);
        nicknameField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-text-fill: white; -fx-prompt-text-fill: #aaaaaa; -fx-alignment: center;"
        );

        Button submitButton = new Button("Submit");
        submitButton.setFont(new Font(18));
        submitButton.setMaxWidth(Double.MAX_VALUE);
        submitButton.setStyle(
                "-fx-background-color: rgba(0, 150, 255, 0.6); -fx-text-fill: white; -fx-background-radius: 8;"
        );

        nicknameField.setOnAction(e -> submitNickname(nicknameField));
        submitButton.setOnAction(e -> submitNickname(nicknameField));

        layout.getChildren().addAll(prompt, nicknameField, submitButton);

        root.setAlignment(Pos.CENTER);
        root.getChildren().add(layout);
        Platform.runLater(nicknameField::requestFocus);
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
