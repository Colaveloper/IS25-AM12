package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;

import java.awt.*;

/**
 * Event representing the initialization of a cabin with a specific crew type.
 *
 * @param playerName the name of the player initializing the cabin
 * @param point the location of the cabin on the ship
 * @param crewType the type of crew assigned to the cabin
 */
public record InitializeCabinEvent(String playerName, Point point, CrewType crewType) implements LobbyEvent {
}
