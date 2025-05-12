package it.polimi.ingsw.galaxytruckers.serverController.events;

/**
 * Game event signaling a specific state change in the whole application.
 * Includes methods to inspect what changed
 */
public interface Event {

    /**
     * Accepts a {@link EventVisitor} and allows it to perform
     * an operation on this event
     * @param visitor the accepted {@link EventVisitor}
     */
    void accept(EventVisitor visitor);
}
