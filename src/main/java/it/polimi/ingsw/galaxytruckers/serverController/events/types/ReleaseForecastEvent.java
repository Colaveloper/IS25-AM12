package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing a player releasing a forecast deck.
 *
 * @param playerName the name of the player releasing the forecast
 * @param deckIndex the index of the forecast deck
 */
public record ReleaseForecastEvent(String playerName, int deckIndex) implements LobbyEvent {
}
