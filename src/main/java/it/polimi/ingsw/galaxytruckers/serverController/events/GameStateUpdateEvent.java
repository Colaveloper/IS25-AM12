package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.serverController.dto.states.StateDTO;

public record GameStateUpdateEvent(StateDTO stateDTO) implements Event{
}
