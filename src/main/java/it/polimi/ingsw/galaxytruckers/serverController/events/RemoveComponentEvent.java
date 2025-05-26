package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record RemoveComponentEvent(String playerName, Point point) implements Event {
    public static RemoveComponentEvent from(ShipBoard shipBoard, Point point) {
        return new RemoveComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point
        );
    }

}
