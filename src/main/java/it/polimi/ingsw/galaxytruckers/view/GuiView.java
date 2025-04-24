package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;

import java.io.IOException;

public class GuiView extends View{

    public GuiView(ClientModel model, VirtualServer server) {
        super(model, server);
    }

    @Override
    public void run(ScreenStrategy strategy) throws IOException {

    }
}
