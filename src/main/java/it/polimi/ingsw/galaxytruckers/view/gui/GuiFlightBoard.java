package it.polimi.ingsw.galaxytruckers.view.gui;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.Node;

public class GuiFlightBoard extends GuiElement {

    public GuiFlightBoard(ClientModel model, VirtualServer server) {
        super(model, server);
    }

    @Override
    public Node getNode() {
        return null;
    }
}
