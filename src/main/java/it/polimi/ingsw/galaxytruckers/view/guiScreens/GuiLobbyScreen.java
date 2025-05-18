package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class GuiLobbyScreen extends GuiScreen {

    public GuiLobbyScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public void attachContentToRoot(VBox root) {
        FlowPane lobbyPane = new FlowPane();
        lobbyPane.setAlignment(Pos.CENTER);
        lobbyPane.setHgap(20);
        lobbyPane.setVgap(20);
        lobbyPane.setPrefWrapLength(600);

        for (String nickname : model.getNicknames()) {
            StackPane playerBox = createPlayerBox(nickname);
            lobbyPane.getChildren().add(playerBox);
        }

        root.getChildren().add(lobbyPane);
    }


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
