package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;

/**
 * Event representing a player's request to surrender, with a specific cause.
 *
 * @param playerName the name of the player requesting to surrender
 * @param cause the reason for the surrender request
 */
public record SurrenderRequestEvent(String playerName, SurrenderCause cause) implements LobbyEvent{
}
