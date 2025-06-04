package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import java.util.UUID;

public record RemoveActiveLobbyEvent(UUID lobbyId) implements ControllerEvent {
}
