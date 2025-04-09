package it.polimi.ingsw.galaxytruckers.networkRMI.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Questa interfaccia specializza l'interfaccia VirtualView per la tecnologia RMI
 */
public interface VirtualViewRmi extends Remote, VirtualView {
    @Override
    void showNewCard(Integer cardId) throws RemoteException;
    @Override
    void reportError(String details) throws RemoteException;
}
