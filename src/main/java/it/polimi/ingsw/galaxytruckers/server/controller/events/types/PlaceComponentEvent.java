package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;

/**
 * Event representing a player placing a component on their ship.
 *
 * @param playerName the name of the player placing the component
 * @param position the position on the ship where the component is placed
 * @param rotation the rotation of the component
 */
public record PlaceComponentEvent(String playerName, Point position, Direction rotation) implements LobbyEvent {
}
