package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GuiView extends View {
    GuiScreen screen;

    public GuiView(ControllerToServer controller, ClientModel model) {
        super(controller, model);
    }

    @Override
    public void updateScreen() {
//        screen = screenFactory.createGuiScreen(model, controller);
//        this.refreshScreen();
    }

    @Override
    public void start() {

    }

    @Override
    public void refresh() {

    }

    private void refreshScreen(VBox root) {
        Platform.runLater(() -> {
            root.getChildren().clear();
            try {
                screen.attachContentToRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
