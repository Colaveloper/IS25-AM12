package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import java.util.UUID;

/**
 * Event representing the removal of an active lobby.
 *
 * @param lobbyId the unique identifier of the lobby to be removed
 */
public record RemoveActiveLobbyEvent(UUID lobbyId) implements ControllerEvent {
}
