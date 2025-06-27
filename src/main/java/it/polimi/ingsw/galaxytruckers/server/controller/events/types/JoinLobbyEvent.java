package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;

/**
 * Event signaling that a player has joined a lobby.
 *
 * @param playerName the nickname of the player who joined
 * @param color the color associated with the player
 */
public record JoinLobbyEvent(String playerName, GameColor color) implements LobbyEvent {
}
