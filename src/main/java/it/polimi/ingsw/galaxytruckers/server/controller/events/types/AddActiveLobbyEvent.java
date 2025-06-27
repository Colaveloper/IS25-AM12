package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import it.polimi.ingsw.galaxytruckers.server.controller.dto.ActiveLobbyDTO;

/**
 * Event representing the addition of a new active lobby.
 * This event is used to notify listeners that a new lobby has been created and is available for players to join.
 *
 * @param newLobby the data transfer object containing information about the newly added lobby
 */
public record AddActiveLobbyEvent(ActiveLobbyDTO newLobby) implements ControllerEvent {
}
