package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;

public record ShipPieceRemoveEvent(String playerName, int index) implements Event {
    public static ShipPieceRemoveEvent from(ShipBoard shipBoard, int index) {
        return new ShipPieceRemoveEvent (
                Player.getPlayer(shipBoard).getNickname(),
                index
        );
    }
}
