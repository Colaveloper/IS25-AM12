package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;

/**
 * Event that represents a player's request to surrender or withdraw from a game.
 * This event is dispatched when a player wants to quit a game in progress,
 * with a specific reason specified by the surrender cause.
 *
 * @param playerName The name of the player requesting to surrender
 * @param cause The reason or cause for the surrender request
 */
public record SurrenderRequestEvent(String playerName, SurrenderCause cause) implements LobbyEvent{
}
