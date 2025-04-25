package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiView extends Application implements View {
    static VirtualServer server;
    static ClientModel model;
    // TODO: privatize and create setter in View
    public static ScreenStrategy firstStrategy;
    static Pane root;  // will be passed to screen strategies


    public GuiView() {
        super();
        GuiView.root = new VBox(); //
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("Galaxy Trucker");
        run(firstStrategy);
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
        strategy.showGUI(model, root);
    }
}
