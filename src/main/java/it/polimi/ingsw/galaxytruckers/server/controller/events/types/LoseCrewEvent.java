package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import java.awt.*;

/**
 * Event representing a player losing crew at a specific location on their ship.
 *
 * @param playerName the name of the player losing crew
 * @param point the location on the ship where crew is lost
 */
public record LoseCrewEvent(String playerName, Point point) implements LobbyEvent {
}
