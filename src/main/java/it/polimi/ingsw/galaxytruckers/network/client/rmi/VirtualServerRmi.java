package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualClient;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.VirtualClientRmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Questa interfaccia specializza l'interfaccia VirtualServer per la tecnologia Socket
 */
public interface VirtualServerRmi extends Remote, VirtualServer {

    void connect(VirtualClientRmi client) throws RemoteException;

    // metodi controller:
    @Override
    void drawCard() throws IOException;

    @Override
    void registerNickname(VirtualClient client, String nickname) throws RemoteException;
}
