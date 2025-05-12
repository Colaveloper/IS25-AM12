package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RemoteClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    RemoteController registerNickname(RemoteClient client, String nickname) throws RemoteException;
}
