package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record GrabStashedComponentEvent(String playerName, int index) implements Event {
    public static GrabStashedComponentEvent from(ShipBoard shipBoard, int index) {
        return new GrabStashedComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                index
        );
    }

}
