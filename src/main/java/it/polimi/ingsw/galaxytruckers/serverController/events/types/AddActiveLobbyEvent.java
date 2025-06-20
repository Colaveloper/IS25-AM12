package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.ActiveLobbyDTO;

public record AddActiveLobbyEvent(ActiveLobbyDTO newLobby) implements ControllerEvent {
}
