package it.polimi.ingsw.galaxytruckers.server.controller.events;

import it.polimi.ingsw.galaxytruckers.server.controller.events.types.Event;

/**
 * Generic interface for event listeners in the Galaxy Truckers game.
 * Implementations of this interface are notified when events occur
 * and can respond appropriately based on the event type.
 *
 * @param <T> the specific type of Event that this listener can receive
 */
public interface EventListener<T extends Event> {
    /**
     * Notifies this listener that an event has occurred.
     * Implementing classes should define the specific behavior
     * for responding to the event.
     *
     * @param event the event that occurred
     */
    void notifyEvent(T event);
}
