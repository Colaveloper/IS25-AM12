package it.polimi.ingsw.galaxytruckers.serverController.events;

public record PeekForecastEvent(String playerName, int forecastIndex) implements Event {
}
