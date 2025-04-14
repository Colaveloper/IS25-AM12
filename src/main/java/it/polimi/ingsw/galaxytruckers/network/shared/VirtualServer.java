package it.polimi.ingsw.galaxytruckers.network.shared;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VirtualServer extends EventListener {
    public void registerNickname(String tempNickname, String newNickname) throws IOException;
    void drawCard(String nickname) throws IOException;
    void reportError(String error) throws RemoteException;
}
