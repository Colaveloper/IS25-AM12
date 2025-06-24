package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing a player peeking at a forecast deck.
 *
 * @param playerName the name of the player peeking at the forecast
 * @param forecastIndex the index of the forecast deck being peeked at
 */
public record PeekForecastEvent(String playerName, int forecastIndex) implements LobbyEvent {
}
