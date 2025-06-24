package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import java.util.UUID;

/**
 * Event that represents the removal of an active lobby from the game system.
 * This event is dispatched when a lobby is deactivated, closed, or terminated,
 * typically when a game ends or when all players have left the lobby.
 *
 * @param lobbyId The unique identifier of the lobby being removed
 */
public record RemoveActiveLobbyEvent(UUID lobbyId) implements ControllerEvent {
}
