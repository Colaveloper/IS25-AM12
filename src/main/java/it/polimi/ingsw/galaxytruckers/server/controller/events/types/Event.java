package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import java.io.Serializable;
import java.util.Optional;

/**
 * Game event signaling a specific state change in the whole application.
 * Includes methods to inspect what changed.
 */
public sealed interface Event extends Serializable permits LobbyEvent, ControllerEvent {
    /**
     * Indicates whether the event should cause event handling to resume.
     *
     * @return true if handling should resume, false otherwise
     */
    default boolean shouldResume() {
        return false;
    }

    /**
     * Returns the name of the receiver for this event, if any.
     *
     * @return an Optional containing the receiver's name, or empty if not applicable
     */
    default Optional<String> getReceiverName() {
        return Optional.empty();
    }
}
