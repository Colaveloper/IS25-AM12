package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.states.StateDTO;

public record GameStateUpdateEvent(StateDTO stateDTO) implements LobbyEvent {
}
