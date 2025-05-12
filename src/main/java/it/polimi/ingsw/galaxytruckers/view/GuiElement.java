package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public abstract class GuiElement {
    protected StackPane node = new StackPane();

    public abstract Node getNode(VirtualServer server) throws IOException;
}
