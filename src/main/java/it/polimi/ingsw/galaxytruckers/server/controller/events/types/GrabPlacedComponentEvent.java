package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

/**
 * Event representing a player grabbing a placed component from their ship.
 *
 * @param playerName the name of the player grabbing the component
 */
public record GrabPlacedComponentEvent(String playerName) implements LobbyEvent{
}
