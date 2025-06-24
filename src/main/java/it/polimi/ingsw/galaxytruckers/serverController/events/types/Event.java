package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import java.io.Serializable;
import java.util.Optional;

/**
 * This is the root interface for all events in the Galaxy Truckers game event system.
 * Events are serializable to allow transmission across the network between server and clients.
 * This sealed interface permits only LobbyEvent and ControllerEvent as direct implementations
 * Events signal specific state changes to the entire application, and includes methods to
 * inspect what changed.
 */
public sealed interface Event extends Serializable permits LobbyEvent, ControllerEvent {

    /**
     * Determines if processing should resume after this event.
     * By default, events do not trigger resumption of paused processes.
     * Implementations can override this to indicate that event processing should continue.
     *
     * @return true if processing should resume after this event, false otherwise
     */
    default boolean shouldResume() {
        return false;
    }

    /**
     * Gets the name of the specific player that should receive this event.
     * By default, events have no specific receiver and are broadcast to all relevant players.
     * Implementations can override this to target specific players.
     *
     * @return an Optional containing the receiver's name if the event has a specific target,
     *         or an empty Optional if the event should be broadcast
     */
    default Optional<String> getReceiverName() {
        return Optional.empty();
    }
}
