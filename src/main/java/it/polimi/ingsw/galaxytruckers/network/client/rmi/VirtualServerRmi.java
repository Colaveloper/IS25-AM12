package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.VirtualClientRmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Questa interfaccia specializza l'interfaccia VirtualServer per la tecnologia Socket
 */
public interface VirtualServerRmi extends Remote, VirtualServer {

    public void connect(VirtualClientRmi client) throws RemoteException;

    // metodi controller:
    @Override
    public void drawCard() throws IOException;

    @Override
    public void registerNickname(String tempNickname, String newNickname) throws RemoteException;
}
