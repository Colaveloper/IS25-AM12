package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualClient;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Questa interfaccia specializza l'interfaccia VirtualView per la tecnologia RMI
 */
public interface RmiVirtualClient extends Remote, VirtualClient {
    @Override
    void showNewCard(Integer cardId) throws IOException;
    @Override
    void reportError(String details) throws RemoteException;
    @Override
    void showNicknameRegistration(String nickname) throws RemoteException;
}
