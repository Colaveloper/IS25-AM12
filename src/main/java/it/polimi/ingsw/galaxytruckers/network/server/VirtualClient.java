package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

/**
 * Represents a virtual client that can receive events.
 * This interface is used to notify clients about events in the game.
 */
public interface VirtualClient {
    /**
     * Notifies the virtual client of an event.
     * This method is called to inform the client about various events in the game.
     *
     * @param event the event to notify the client about
     */
    void notifyEvent(Event event);
}
