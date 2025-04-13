package it.polimi.ingsw.galaxytruckers.view.screen;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;

import java.rmi.RemoteException;

public class WelcomeScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {

    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws RemoteException {

    }
}
