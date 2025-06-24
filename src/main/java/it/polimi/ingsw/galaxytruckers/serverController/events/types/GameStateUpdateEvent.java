package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.states.StateDTO;

/**
 * Event that represents an update to the overall game state.
 * This event is dispatched when there is a significant change in the game state
 * that needs to be communicated to all players in a lobby.
 *
 * @param stateDTO The data transfer object containing the updated game state information
 */
public record GameStateUpdateEvent(StateDTO stateDTO) implements LobbyEvent {
}
