package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import java.awt.*;

/**
 * Event representing a player using a battery at a specific location on their ship.
 *
 * @param playerName the name of the player using the battery
 * @param point the location on the ship where the battery is used
 */
public record UseBatteryEvent(String playerName, Point point) implements LobbyEvent {
}
