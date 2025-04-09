package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.VirtualClientRmi;

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
