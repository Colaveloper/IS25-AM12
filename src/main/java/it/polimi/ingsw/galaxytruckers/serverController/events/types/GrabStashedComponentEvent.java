package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record GrabStashedComponentEvent(String playerName, int index) implements LobbyEvent {
    public static GrabStashedComponentEvent from(ShipBoard shipBoard, int index) {
        return new GrabStashedComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                index
        );
    }

}
