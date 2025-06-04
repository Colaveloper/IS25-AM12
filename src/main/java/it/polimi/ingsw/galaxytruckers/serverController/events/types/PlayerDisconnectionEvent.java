package it.polimi.ingsw.galaxytruckers.serverController.events.types;

public record PlayerDisconnectionEvent(String playerName) implements LobbyEvent {
}
