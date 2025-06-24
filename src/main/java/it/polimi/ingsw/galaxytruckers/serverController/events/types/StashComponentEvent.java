package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing a player stashing a component.
 *
 * @param playerName the name of the player stashing the component
 */
public record StashComponentEvent(String playerName) implements LobbyEvent {
}
