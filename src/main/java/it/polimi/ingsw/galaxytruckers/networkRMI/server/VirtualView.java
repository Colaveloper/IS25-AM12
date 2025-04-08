package it.polimi.ingsw.galaxytruckers.networkRMI.server;

import java.rmi.RemoteException;

public interface VirtualView {
    void showNewCardUpdate(Integer cardId) throws RemoteException;
    void reportError(String details) throws Exception;
}
