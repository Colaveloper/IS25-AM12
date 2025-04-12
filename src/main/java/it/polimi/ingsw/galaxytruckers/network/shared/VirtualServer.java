package it.polimi.ingsw.galaxytruckers.network.shared;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VirtualServer extends EventListener {
    void registerNickname(VirtualClient client, String nickname) throws RemoteException;
    void drawCard() throws IOException;
    void reportError(String error) throws RemoteException;
}
