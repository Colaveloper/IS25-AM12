package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDTO;

import java.util.List;

public record SetActiveLobbiesEvent(String playerName, List<LobbyDTO> activeLobbies) implements ControllerEvent {
}
