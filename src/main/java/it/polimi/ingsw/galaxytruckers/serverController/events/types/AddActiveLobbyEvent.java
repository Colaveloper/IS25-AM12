package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.ActiveLobbyDTO;

/**
 * Event that represents the addition of a new active lobby to the system.
 * This event is dispatched when a new lobby is created and becomes active in the game.
 *
 * @param newLobby The DTO containing information about the newly created active lobby
 */
public record AddActiveLobbyEvent(ActiveLobbyDTO newLobby) implements ControllerEvent {
}
