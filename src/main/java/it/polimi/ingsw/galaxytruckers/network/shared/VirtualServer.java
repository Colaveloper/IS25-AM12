package it.polimi.ingsw.galaxytruckers.network.shared;

import it.polimi.ingsw.galaxytruckers.network.server.rmi.VirtualClientRmi;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VirtualServer extends EventListener {
    public void registerNickname(String tempNickname, String newNickname) throws RemoteException;
    void drawCard() throws IOException;
    void reportError(String error) throws RemoteException;
}
