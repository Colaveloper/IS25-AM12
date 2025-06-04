package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDTO;

public record AddActiveLobbyEvent(LobbyDTO newLobby) implements ControllerEvent {
}
