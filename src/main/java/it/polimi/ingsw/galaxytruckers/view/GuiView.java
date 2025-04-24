package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiView extends Application implements View {
    static VirtualServer server;
    static ClientModel model;

    public GuiView() {
        super();
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox cardContainer = new HBox(20);
        model.setCurrentCard(1);
        Physical currentCard = model.getCurrentCard();
        cardContainer.getChildren().addAll(currentCard.getNode());
        VBox root = new VBox(10, cardContainer);
        stage.setScene(new Scene(root, 200, 100));
        stage.setTitle("Card Viewer");
        stage.show();
    }

    @Override
    public void setModel(ClientModel model) {
        GuiView.model = model;
    }

    @Override
    public void setServer(VirtualServer server) {
        GuiView.server = server;
    }

    @Override
    public void run(ScreenStrategy strategy) throws IOException {

    }
}
