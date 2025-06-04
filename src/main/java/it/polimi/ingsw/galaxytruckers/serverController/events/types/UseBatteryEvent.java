package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record UseBatteryEvent(String playerName, Point point) implements LobbyEvent {
    public static UseBatteryEvent from(ShipBoard shipBoard, Point point) {
        return new UseBatteryEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point
        );
    }

}
