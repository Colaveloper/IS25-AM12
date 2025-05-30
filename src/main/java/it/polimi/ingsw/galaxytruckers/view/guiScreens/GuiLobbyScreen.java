package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GuiLobbyScreen extends GuiScreen {

    public GuiLobbyScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }



    @Override
    public Parent getNode() {
        FlowPane lobbyPane = new FlowPane();
        lobbyPane.setAlignment(Pos.CENTER);
        lobbyPane.setHgap(20);
        lobbyPane.setVgap(20);
        lobbyPane.setPrefWrapLength(600);

        for (Player player : model.getPlayers()) {
            StackPane playerBox = createPlayerBox(player.getNickname());
            lobbyPane.getChildren().add(playerBox);
        }

        return lobbyPane;
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
