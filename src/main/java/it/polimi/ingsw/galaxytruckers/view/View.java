package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;

import java.io.IOException;

public interface View {
    // this is not an abstract class in order to let GuiView extend Application
    void setModel(ClientModel model);
    void setServer(VirtualServer server);

    /**
     * Provides to the user a contract to interact with the game
     * @throws IOException
     */
    void run(ScreenStrategy strategy) throws IOException;
}
