package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.server.controller.events.types.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface used by the server to notify the client of events
 * using RMI.
 */
public interface RemoteClient extends Remote {
    /**
     * Notifies the client of an event.
     *
     * @param event the event to notify
     * @throws RemoteException if a remote communication error occurs
     */
    void notifyEvent(Event event) throws RemoteException;
}
