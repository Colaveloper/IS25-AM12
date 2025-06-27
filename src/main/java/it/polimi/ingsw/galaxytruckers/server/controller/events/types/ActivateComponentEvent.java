package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import java.awt.*;

/**
 * Event representing the activation or deactivation of a component
 * by a player at a specific point on the board.
 *
 * @param playerName the name of the player performing the action
 * @param point the location of the component to activate/deactivate
 * @param active true if the component is being activated, false if deactivated
 */
public record ActivateComponentEvent(String playerName, Point point, boolean active) implements LobbyEvent {
}