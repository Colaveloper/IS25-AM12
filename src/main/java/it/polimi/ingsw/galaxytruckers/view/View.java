package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;

import java.io.IOException;

public abstract class View {
    final ClientModel model;
    final VirtualServer server;

    public View (ClientModel model, VirtualServer server) {
        this.model = model;
        this.server = server;
    }

    public abstract void run(ScreenStrategy strategy) throws IOException;
}
