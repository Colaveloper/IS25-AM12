package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.scene.layout.VBox;

import java.io.IOException;

public abstract class GuiScreen {
    protected final ClientModel model;
    protected final ClientController controller;
    public GuiScreen(ClientModel model, ClientController controller, GameState gameState) {
        this.model = model;
        this.controller = controller;
    }
    public abstract void attachContentToRoot(VBox root) throws IOException;
}
