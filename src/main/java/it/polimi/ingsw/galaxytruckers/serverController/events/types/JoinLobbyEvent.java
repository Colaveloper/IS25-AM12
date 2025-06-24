package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;

/**
 * Event signaling that a player has joined a lobby.
 *
 * @param playerName the nickname of the player who joined
 * @param color the color associated with the player
 */
public record JoinLobbyEvent(String playerName, GameColor color) implements LobbyEvent {
}
