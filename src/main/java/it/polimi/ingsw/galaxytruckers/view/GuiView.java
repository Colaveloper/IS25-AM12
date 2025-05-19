package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.screens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiView extends Application implements View {
    static ClientController controller;
    static ClientModel model;
    // TODO: privatize and create setter in View
    public static GuiScreen screen;
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
        refreshScreen();
        stage.show();
    }

    @Override
    public void setModel(ClientModel model) {
        GuiView.model = model;
    }

    @Override
    public void setController(ClientController controller) {
        GuiView.controller = controller;
    }


    /**
     * Modifies the objects on screen as prescribed by {@code strategy}
     * @throws IOException
     */
    @Override
    public void setScreen(ScreenFactory screenFactory) {
        screen = screenFactory.getGuiScreen(model, controller);
        this.refreshScreen();
    }

    private void refreshScreen() {
        Platform.runLater(() -> {
            root.getChildren().clear();
            try {
                screen.attachContentToRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e); // TODO: specify error
            }
        });
    }
}