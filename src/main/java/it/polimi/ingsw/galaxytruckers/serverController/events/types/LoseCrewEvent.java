package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import java.awt.*;

/**
 * Event that represents a player losing a crew member from their ship.
 * This event is dispatched when a crew member is lost during gameplay,
 * typically due to damage, combat, or adventure card effects.
 *
 * @param playerName The name of the player who lost the crew member
 * @param point The coordinates on the ship board where the crew member was lost
 */
public record LoseCrewEvent(String playerName, Point point) implements LobbyEvent {
}
