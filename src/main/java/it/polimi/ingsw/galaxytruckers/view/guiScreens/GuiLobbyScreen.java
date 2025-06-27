package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * GUI screen for displaying the game lobby.
 * This screen shows all players who have joined the game and are waiting
 * for the game to start. Each player is represented by a visual box containing
 * their nickname.
 */
public class GuiLobbyScreen extends GuiScreen {
    private FlowPane lobbyPane;

    /**
     * Constructs a new lobby screen.
     *
     * @param model      The client model containing game state and player information
     * @param controller The controller for communicating with the server
     */
    public GuiLobbyScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    /**
     * {@inheritDoc}
     * Creates and returns the lobby interface displaying all players in the lobby.
     * Players are shown in a grid layout with their nicknames.
     *
     * @return A Pane containing the lobby user interface with player boxes
     */
    @Override
    public Pane getNode() {
        lobbyPane = new FlowPane();
        lobbyPane.setAlignment(Pos.CENTER);
        lobbyPane.setHgap(20);
        lobbyPane.setVgap(20);
        lobbyPane.setPrefWrapLength(600);

        for (Player player : model.getPlayers()) {
            StackPane playerBox = createPlayerBox(player.getNickname());
            lobbyPane.getChildren().add(playerBox);
        }

        Button leaveGameButton = new Button("Leave Game");
        leaveGameButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        leaveGameButton.setOnAction(e -> {
            controller.quit();
        });

        VBox root = new VBox(20, lobbyPane, leaveGameButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        return root;
    }

    @Override
    public void notifyPlayerJoin(Player player) {
        Platform.runLater(() -> {
            StackPane playerBox = createPlayerBox(player.getNickname());
            lobbyPane.getChildren().add(playerBox);
        });
    }

    /**
     * Creates a styled box representing a player in the lobby.
     *
     * @param nickname The nickname of the player to display
     * @return A StackPane containing the player's information with appropriate styling
     */
    private StackPane createPlayerBox(String nickname) {
        Rectangle background = new Rectangle(150, 100);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(Color.LIGHTGRAY);

        Label nameLabel = new Label(nickname);

        StackPane box = new StackPane(background, nameLabel);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
