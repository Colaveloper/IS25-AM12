package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;

/**
 * Event signaling that one or more players have surrendered.
 *
 * @param playerNames list of nicknames of the players that surrendered
 */
public record SurrenderEvent(List<String> playerNames) implements LobbyEvent {
}
