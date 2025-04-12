package it.polimi.ingsw.galaxytruckers.network.shared;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EventListener extends Remote {
    void registerHandler(EventHandler handler) throws RemoteException;
    void processEvents() throws IOException;
}
