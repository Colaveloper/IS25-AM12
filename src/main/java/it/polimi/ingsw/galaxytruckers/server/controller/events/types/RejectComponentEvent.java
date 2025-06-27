package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

/**
 * Event representing a player rejecting a component.
 *
 * @param playerName the name of the player rejecting the component
 */
public record RejectComponentEvent(String playerName) implements LobbyEvent {
}
