package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public abstract class GuiElement {
    protected final ClientController controller;
    protected final ClientModel model;
    protected StackPane node = new StackPane();

    public GuiElement(ClientModel model, ClientController controller) {
        this.model = model;
        this.controller = controller;
    }

    public abstract Node getNode() throws IOException;
}
