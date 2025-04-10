package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.network.server.VirtualClient;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VirtualServer {
    void registerNickname(VirtualClient client, String nickname) throws RemoteException;
    void drawCard() throws IOException;
    void reportError(String error) throws RemoteException;
}
