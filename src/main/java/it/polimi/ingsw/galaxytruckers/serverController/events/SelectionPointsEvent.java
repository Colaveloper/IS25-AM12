package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;

public record SelectionPointsEvent(String playerName, List<Point> points) implements Event {
    public static SelectionPointsEvent from(ShipBoard shipBoard, List<Point> points) {
        return new SelectionPointsEvent(
                Player.getPlayer(shipBoard).getNickname(),
                points
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
