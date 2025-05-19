package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.UiElement;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public abstract class GuiElement extends UiElement {
    protected final ClientController controller;

    public GuiElement(ClientModel model, ClientController controller) {
        super(model);
        this.controller = controller;
    }

    public abstract Node getNode() throws IOException;
}
