package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record StashComponentEvent(String playerName) implements LobbyEvent {
    public static StashComponentEvent from(ShipBoard shipBoard) {
        return new StashComponentEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
