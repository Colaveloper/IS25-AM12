package it.polimi.ingsw.galaxytruckers.server.controller.events;

import it.polimi.ingsw.galaxytruckers.server.controller.events.types.Event;

/**
 * Generic interface for event handlers in the Galaxy Truckers game.
 * Implementations of this interface are responsible for processing
 * and responding to specific types of events in the event system.
 *
 * @param <T> the specific type of Event that this handler can process
 */
public interface EventHandler<T extends Event> {
    /**
     * Handles an event of the specified type.
     * Implementing classes should define the specific behavior for processing the event.
     *
     * @param event the event to be handled
     */
    void handleEvent(T event);
}
