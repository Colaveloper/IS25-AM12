package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiView extends Application implements View {
    static VirtualServer server;
    static ClientModel model;
    // TODO: privatize and create setter in View
    public static ScreenStrategy strategy;
    static Pane root;  // will be passed to screen strategies


    public GuiView() {
        super();
        GuiView.root = new VBox(); // immediately reassigned. used just to avoid NullPointerException
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(GuiView.root);
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

    // TODO: delete
    @Override
    public void refresh() {

    }

}