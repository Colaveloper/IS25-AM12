package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.states.StateDTO;

/**
 * Event representing an update to the overall game state.
 *
 * @param stateDTO the data transfer object containing the new game state
 */
public record GameStateUpdateEvent(StateDTO stateDTO) implements LobbyEvent {
}
