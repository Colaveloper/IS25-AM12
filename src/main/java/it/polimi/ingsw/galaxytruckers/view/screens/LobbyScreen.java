package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

import java.io.IOException;

public class LobbyScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {
        System.out.print("Currently in lobby: "+model.getNicknames());
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {}

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) {
        root.getChildren().clear();

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
