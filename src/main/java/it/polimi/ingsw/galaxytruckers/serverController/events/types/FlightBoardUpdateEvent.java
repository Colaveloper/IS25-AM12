package it.polimi.ingsw.galaxytruckers.serverController.events.types;

public record FlightBoardUpdateEvent(String playerName, int position) implements LobbyEvent {
}
