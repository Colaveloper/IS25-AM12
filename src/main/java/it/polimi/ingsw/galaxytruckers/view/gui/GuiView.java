package it.polimi.ingsw.galaxytruckers.view.gui;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.View;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiView extends Application implements View {
    static VirtualServer server;
    static ClientModel model;
    // TODO: privatize and create setter in View
    public static ScreenStrategy strategy;
    static VBox root;  // will be passed to screen strategies


    public GuiView() {
        super();
        GuiView.root = new VBox(); // immediately reassigned. used just to avoid NullPointerException
        root.setPadding(new Insets(50));
        root.setSpacing(50);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(GuiView.root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Galaxy Trucker");
        setScreen(strategy); // TODO: to avoid this, separate setting the screen and calling a rerender
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


    /**
     * Modifies the objects on screen as prescribed by {@code strategy}
     * @throws IOException
     */
    @Override
    public void setScreen(ScreenStrategy newStrategy) throws IOException {
//        if (!newStrategy.equals(strategy)) {
            strategy = newStrategy;
            Platform.runLater(() -> {
                root.getChildren().clear();
                try {
                    strategy.showGUI(model, root, server);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
//        }
    }
}