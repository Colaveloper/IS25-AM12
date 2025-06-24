package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import java.awt.*;

/**
 * Event representing the removal of a component from a player's ship.
 *
 * @param playerName the name of the player removing the component
 * @param point the location on the ship where the component is removed
 */
public record RemoveComponentEvent(String playerName, Point point) implements LobbyEvent {
}
