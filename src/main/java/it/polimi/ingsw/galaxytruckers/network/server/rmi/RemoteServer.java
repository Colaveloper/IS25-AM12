package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for the server, allowing clients to register their nicknames.
 */
public interface RemoteServer extends Remote {
    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#registerNickname(String)}
     * @param client the remote client that is registering the nickname
     * @param nickname the nickname to register
     * @return a RemoteController that allows the client to interact with the server
     * @throws RemoteException if there is an error during the remote method call
     */
    RemoteController registerNickname(RemoteClient client, String nickname) throws RemoteException;
}
