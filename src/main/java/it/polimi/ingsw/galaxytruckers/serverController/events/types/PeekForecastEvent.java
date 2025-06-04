package it.polimi.ingsw.galaxytruckers.serverController.events.types;

public record PeekForecastEvent(String playerName, int forecastIndex) implements LobbyEvent {
}
