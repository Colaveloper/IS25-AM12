package it.polimi.ingsw.galaxytruckers.serverController.events.types;

public record CurrentPlayerUpdateEvent(String playerName) implements LobbyEvent {
}
