package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.UUID;

public class GuiJoinOrCreateScreen extends GuiScreen {

    public GuiJoinOrCreateScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    @Override
    public Parent getNode() {
        Label title = new Label("Are you ready for a new adventure?");
        title.setFont(new Font("Arial", 24));
        title.setWrapText(true);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTextFill(Color.LIGHTSKYBLUE);
        title.setMaxWidth(400);
        title.setEffect(new DropShadow(10, Color.DARKSLATEBLUE));

        // CREATE button
        Button createButton = new Button("CREATE NEW GAME");
        createButton.setFont(new Font(18));
        createButton.setMaxWidth(Double.MAX_VALUE);
        createButton.setStyle(
                "-fx-background-color: rgba(80, 0, 200, 0.6); -fx-text-fill: white; -fx-background-radius: 8;"
        );
        createButton.setOnAction(e -> controller.showGameCreation());

        // JOIN section
        TextField lobbyField = new TextField();
        lobbyField.setPromptText("Enter lobby name");
        lobbyField.setMaxWidth(Double.MAX_VALUE);
        lobbyField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-text-fill: white; -fx-prompt-text-fill: #aaaaaa; -fx-alignment: center;"
        );

        Button joinButton = new Button("JOIN");
        joinButton.setFont(new Font(18));
        joinButton.setMaxWidth(Double.MAX_VALUE);
        joinButton.setStyle(
                "-fx-background-color: rgba(0, 150, 255, 0.6); -fx-text-fill: white; -fx-background-radius: 8;"
        );
        joinButton.setOnAction(e -> controller.joinLobby(UUID.fromString(lobbyField.getText())));

        // Main layout
        VBox contentBox = new VBox(20, title, createButton, lobbyField, joinButton);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));
        contentBox.setMaxWidth(400);
        contentBox.setStyle(
                "-fx-background-color: rgba(0, 0, 50, 0.4); -fx-background-radius: 20;"
        );

        return contentBox;
    }
}
