package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

/**
 * Interface for client-side controller used to receive updates from the server
 * during the game lifecycle in Galaxy Truckers.
 */
public interface ClientControllerInterface {

    /**
     * Notifies the client of an event that occurred on the server.
     *
     * @param event the event to notify the client about
     */
    void notifyEvent(Event event);

    /**
     * Notifies the client that they have been disconnected from the server.
     */
    void signalDisconnection();

}