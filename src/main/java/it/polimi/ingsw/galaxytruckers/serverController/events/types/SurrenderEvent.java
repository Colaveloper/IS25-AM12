package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;

/**
 * ModelEvent signaling one or more players have surrendered
 * @param playerNames list of nicknames of the players that surrendered
 */
public record SurrenderEvent(List<String> playerNames) implements LobbyEvent {
    public static SurrenderEvent from(List<ShipBoard> ships) {
        return new SurrenderEvent(ships.stream()
                        .map(s -> Player.getPlayer(s).getNickname())
                        .toList());
    }

}
