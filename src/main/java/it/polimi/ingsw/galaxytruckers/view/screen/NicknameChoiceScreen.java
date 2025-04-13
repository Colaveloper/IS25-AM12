package it.polimi.ingsw.galaxytruckers.view.screen;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;

import java.rmi.RemoteException;

public class NicknameChoiceScreen implements ScreenStrategy {
    @Override
    public void showCLI(ClientModel model) {
        System.out.println("Successfully bound to the server ✅");
        System.out.println("Please choose a unique nickname in order to proceed: ");
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws RemoteException {
        // passing temporary nickname as first parameter
        server.registerNickname(model.getMyNickname(), input);
    }
}
