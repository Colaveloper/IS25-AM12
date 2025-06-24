package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event that represents a player peeking at a specific card in the forecast deck.
 * This event is dispatched when a player uses an ability to look at a particular
 * set of upcoming adventure cards to prepare for future encounters.
 *
 * @param playerName   The name of the player who is peeking at the forecast
 * @param forecastIndex The index of the card in the forecast deck to peek at
 */
public record PeekForecastEvent(String playerName, int forecastIndex) implements LobbyEvent {
}
