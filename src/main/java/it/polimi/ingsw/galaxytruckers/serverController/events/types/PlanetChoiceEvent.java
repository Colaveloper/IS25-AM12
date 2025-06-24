package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing a player's choice of a planet.
 *
 * @param playerName the name of the player making the choice
 * @param planetIndex the index of the chosen planet
 * @param nextPlayerName the name of the next player to choose
 */
public record PlanetChoiceEvent(String playerName, int planetIndex, String nextPlayerName) implements LobbyEvent {
}
