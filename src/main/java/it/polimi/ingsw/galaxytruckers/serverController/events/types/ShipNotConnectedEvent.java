package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Event representing a player's ship having disconnected pieces.
 *
 * @param playerName the name of the player with disconnected ship pieces
 * @param shipPieces a list of sets of points, each set representing a disconnected group of ship pieces
 */
public record ShipNotConnectedEvent(String playerName, List<Set<Point>> shipPieces) implements LobbyEvent {
}
