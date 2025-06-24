package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing a player grabbing a component from their stash.
 *
 * @param playerName the name of the player grabbing the stashed component
 * @param index the index of the component in the stash
 */
public record GrabStashedComponentEvent(String playerName, int index) implements LobbyEvent {
}
