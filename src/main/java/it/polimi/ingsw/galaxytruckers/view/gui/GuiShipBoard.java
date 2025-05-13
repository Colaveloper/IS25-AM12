package it.polimi.ingsw.galaxytruckers.view.gui;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.Node;

import java.awt.*;
import java.util.List;

public class GuiShipBoard extends GuiElement {
    GuiShipBoard(ClientModel model, VirtualServer server) {
        super(model, server);
    }

    @Override
    public Node getNode() {
        return null;
    }
}
