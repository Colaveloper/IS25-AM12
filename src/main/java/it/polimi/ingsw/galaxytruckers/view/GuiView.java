package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiView extends Application implements View {
    GuiScreen screen;
    ClientModel model;
    ClientController controller;
    ScreenFactory screenFactory;

    public GuiView(ClientController controller, ClientModel model) {
        this.controller = controller;
        this.model = model;
        this.screenFactory = new ScreenFactory();
    }


    @Override
    public void updateScreen() {
        screen = screenFactory.createGuiScreen(model, controller);
//        this.refreshScreen();
    }

//    private void refreshScreen(VBox root) {
//        Platform.runLater(() -> {
//            root.getChildren().clear();
//            try {
//                screen.attachContentToRoot(root);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
