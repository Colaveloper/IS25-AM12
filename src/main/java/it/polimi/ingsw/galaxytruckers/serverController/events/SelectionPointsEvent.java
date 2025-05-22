package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;

public record SelectionPointsEvent(String playerName, List<Point> points, List<Point> batteries) implements Event {
    public static SelectionPointsEvent from(ShipBoard shipBoard, List<Point> points, List<Point> batteries) {
        return new SelectionPointsEvent(
                Player.getPlayer(shipBoard).getNickname(),
                points,
                batteries
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
