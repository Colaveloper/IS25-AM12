package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;

public interface ScreenStrategy {
    void showCLI(ClientModel model);
    boolean isLegalInput(ClientModel model, String input);
    void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException;
    void showGUI(ClientModel model, Pane root, VirtualServer server) throws IOException;
}
