package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Lobby;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GuiJoinOrCreateScreen extends GuiScreen {

    private final List<UUID> ids;
    private final VBox lobbiesBox;

    public GuiJoinOrCreateScreen(ClientModel model, Set<UUID> ids, ControllerToServer controller) {
        super(model, controller);
        this.ids = new ArrayList<>(ids);
        this.lobbiesBox = new VBox(10);
        this.lobbiesBox.setAlignment(Pos.CENTER);
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

        Button createButton = new Button("CREATE NEW GAME");
        createButton.setFont(new Font(18));
        createButton.setMaxWidth(Double.MAX_VALUE);
        createButton.setStyle(
                "-fx-background-color: rgba(80, 0, 200, 0.6); -fx-text-fill: white; -fx-background-radius: 8;"
        );
        createButton.setOnAction(e -> controller.showGameCreation());

        VBox contentBox = new VBox(20, title, createButton, lobbiesBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));
        contentBox.setMaxWidth(400);
        contentBox.setStyle(
                "-fx-background-color: rgba(0, 0, 50, 0.4); -fx-background-radius: 20;"
        );

        updateLobbyButtons();

        return contentBox;
    }

    private void updateLobbyButtons() {
        Platform.runLater(() -> {
            lobbiesBox.getChildren().clear();
            for (UUID id : ids) {
                Button lobbyButton = new Button(id.toString());
                lobbyButton.setFont(new Font(16));
                lobbyButton.setMaxWidth(Double.MAX_VALUE);
                lobbyButton.setStyle(
                        "-fx-background-color: rgba(0, 150, 255, 0.6); -fx-text-fill: white; -fx-background-radius: 8;"
                );
                lobbyButton.setOnAction(e -> controller.joinLobby(id));
                lobbiesBox.getChildren().add(lobbyButton);
            }
        });
    }

    @Override
    public void notifyNewLobby(Lobby lobby) {
        ids.add(lobby.getId());
        updateLobbyButtons();
    }

    @Override
    public void notifyRemoveLobby(UUID lobbyId) {
        ids.remove(lobbyId);
        updateLobbyButtons();
    }
}
