package it.polimi.ingsw.galaxytruckers.view.screen;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;

import java.io.IOException;
import java.rmi.RemoteException;

public interface ScreenStrategy {
    void showCLI(ClientModel model);
    boolean isLegalInput(ClientModel model, String input);
    void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException;

//    void showGUI(ClientModel model);
}
