package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * GUI screen for nickname selection.
 * This screen allows players to enter and submit a unique nickname
 * before joining a game. It represents the first interactive screen
 * that players encounter when starting the game.
 */
public class GuiNicknameChoiceScreen extends GuiScreen {

    /**
     * Constructs a new nickname choice screen.
     *
     * @param model      The client model containing game state
     * @param controller The controller for communicating with the server
     */
    public GuiNicknameChoiceScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    /**
     * Submits the entered nickname to the server if it's not empty.
     * Trims whitespace from the nickname before submission.
     *
     * @param field The text field containing the nickname to submit
     */
    private void submitNickname(TextField field) {
        String nickname = field.getText().trim();
        if (!nickname.isEmpty()) {
            controller.registerNickname(nickname);
        }
    }

    /**
     * {@inheritDoc}
     * Creates and returns the nickname selection interface with a prompt,
     * text field for input, and a submit button.
     *
     * @return A styled Pane containing the nickname selection user interface
     */
    @Override
    public Pane getNode() {
        VBox layout = new VBox(20);
        layout.setMinHeight(Region.USE_PREF_SIZE);
        layout.setMaxHeight(Region.USE_PREF_SIZE);
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

        Platform.runLater(nicknameField::requestFocus);
        return layout;
    }
}
