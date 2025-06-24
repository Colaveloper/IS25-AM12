package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event representing a player rejecting a component.
 *
 * @param playerName the name of the player rejecting the component
 */
public record RejectComponentEvent(String playerName) implements LobbyEvent {
}
