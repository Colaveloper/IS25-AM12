package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import java.io.Serializable;
import java.util.Optional;

/**
 * Game event signaling a specific state change in the whole application.
 * Includes methods to inspect what changed
 */
public sealed interface Event extends Serializable permits LobbyEvent, ControllerEvent {
    default Optional<String> getReceiverName() {
        return Optional.empty();
    }
}
