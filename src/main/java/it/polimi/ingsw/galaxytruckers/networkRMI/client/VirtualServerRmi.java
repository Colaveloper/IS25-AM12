package it.polimi.ingsw.galaxytruckers.networkRMI.client;

import it.polimi.ingsw.galaxytruckers.networkRMI.server.VirtualClientRmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Questa interfaccia specializza l'interfaccia VirtualServer per la tecnologia Socket
 */
public interface VirtualServerRmi extends Remote, VirtualServer {

    void connect(VirtualClientRmi client) throws RemoteException;

    // metodi controller:
    @Override
    void drawCard() throws RemoteException;
}
