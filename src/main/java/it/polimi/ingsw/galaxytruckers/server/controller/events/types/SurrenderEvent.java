package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import java.util.List;

/**
 * Event signaling that one or more players have surrendered.
 *
 * @param playerNames list of nicknames of the players that surrendered
 */
public record SurrenderEvent(List<String> playerNames) implements LobbyEvent {
}
