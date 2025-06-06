package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Screen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public abstract class GuiScreen extends Screen {
    protected final ClientModel model;
    protected final ControllerToServer controller;
    protected final GameState state;

    public GuiScreen(ClientModel model, ControllerToServer controller, GameState state) {
        this.model = model;
        this.controller = controller;
        this.state = state;
 }

    public GuiScreen(ClientModel model, ControllerToServer controller) {
        this(model, controller, null);
    }

    public abstract Parent getNode();
}

