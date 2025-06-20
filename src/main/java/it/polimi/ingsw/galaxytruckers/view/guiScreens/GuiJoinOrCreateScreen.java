package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Lobby;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
    public Pane getNode() {
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

        VBox layout = new VBox(20, title, createButton, lobbiesBox);
        layout.setMinHeight(Region.USE_PREF_SIZE);
        layout.setMaxHeight(Region.USE_PREF_SIZE);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setMaxWidth(400);
        layout.setStyle(
                "-fx-background-color: rgba(0, 0, 50, 0.4); -fx-background-radius: 20;"
        );

        updateLobbyButtons();

        return layout;
    }

    private void updateLobbyButtons() {
        Platform.runLater(() -> {
            lobbiesBox.getChildren().clear();

            // lobby container
            VBox tableContainer = new VBox(0);  // No spacing between rows
            tableContainer.setStyle("-fx-background-color: rgba(10, 20, 50, 0.7); -fx-background-radius: 8;");
            tableContainer.setPadding(new Insets(10));
            tableContainer.setMaxWidth(400);

            // header row
            HBox headerRow = new HBox();
            headerRow.setAlignment(Pos.CENTER_LEFT);
            headerRow.setSpacing(15);  // Increased spacing between columns
            headerRow.setPadding(new Insets(10, 15, 10, 20));
            headerRow.setStyle("-fx-border-color: transparent transparent rgba(200, 200, 200, 0.3) transparent;");

            // host column
            Label hostLabel = new Label("Host");
            hostLabel.setFont(new Font("Arial", 14));
            hostLabel.setTextFill(Color.WHITE);
            hostLabel.setPrefWidth(110);

            // level column
            Label levelLabel = new Label("Game Level");
            levelLabel.setFont(new Font("Arial", 14));
            levelLabel.setTextFill(Color.WHITE);
            levelLabel.setPrefWidth(130);  // Increased width

            // connected players column
            Label playersLabel = new Label("Connected");
            playersLabel.setFont(new Font("Arial", 14));
            playersLabel.setTextFill(Color.WHITE);
            playersLabel.setPrefWidth(140);

            headerRow.getChildren().addAll(hostLabel, levelLabel, playersLabel);
            tableContainer.getChildren().add(headerRow);

            // adding each lobby as a row
            for (UUID id : ids) {
                Lobby lobby = model.getActiveLobbies().get(id);
                if (lobby != null) {
                    HBox lobbyRow = createLobbyRow(lobby, id);
                    tableContainer.getChildren().add(lobbyRow);
                }
            }
            lobbiesBox.getChildren().add(tableContainer);

            // message if no lobbies are available
            if (ids.isEmpty()) {
                Label noLobbiesLabel = new Label("No active games available. Create a new one!");
                noLobbiesLabel.setTextFill(Color.LIGHTGRAY);
                noLobbiesLabel.setFont(new Font("Arial", 14));
                noLobbiesLabel.setPadding(new Insets(20, 0, 0, 0));
                tableContainer.getChildren().add(noLobbiesLabel);
            }
        });
    }

    private HBox createLobbyRow(Lobby lobby, UUID id) {
        HBox rowContent = new HBox();
        rowContent.setAlignment(Pos.CENTER_LEFT);
        rowContent.setSpacing(10);
        rowContent.setPadding(new Insets(12, 10, 12, 20));

        // change background color on hover
        rowContent.setOnMouseEntered(e ->
            rowContent.setStyle("-fx-background-color: rgba(60, 100, 160, 0.6);")
        );
        rowContent.setOnMouseExited(e ->
            rowContent.setStyle("-fx-background-color: rgba(30, 40, 70, 0.6);")
        );

        // host column
        Label hostValue = new Label(lobby.getHost());
        hostValue.setFont(new Font("Arial", 14));
        hostValue.setTextFill(Color.WHITE);
        hostValue.setPrefWidth(140);

        // level column
        Label levelValue = new Label(lobby.getLevel().toString());
        levelValue.setFont(new Font("Arial", 14));
        levelValue.setTextFill(Color.WHITE);
        levelValue.setPrefWidth(140);

        // connected players column
        int connectedPlayers = lobby.getPlayers().size();
        Label playersValue = new Label(connectedPlayers + "/" + lobby.getPlayersN());
        playersValue.setFont(new Font("Arial", 14));
        playersValue.setTextFill(Color.WHITE);
        playersValue.setPrefWidth(120);

        rowContent.getChildren().addAll(hostValue, levelValue, playersValue);
        rowContent.setOnMouseClicked(e -> controller.joinLobby(id));
        rowContent.setCursor(javafx.scene.Cursor.HAND);

        return rowContent;
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
