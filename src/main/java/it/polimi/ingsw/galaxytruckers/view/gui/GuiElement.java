package it.polimi.ingsw.galaxytruckers.view.gui;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public abstract class GuiElement {
    protected final VirtualServer server;
    protected final ClientModel model;

    public GuiElement(ClientModel model, VirtualServer server) {
        this.model = model;
        this.server = server;
    }

    protected StackPane node = new StackPane();
    public abstract Node getNode() throws IOException;
}
