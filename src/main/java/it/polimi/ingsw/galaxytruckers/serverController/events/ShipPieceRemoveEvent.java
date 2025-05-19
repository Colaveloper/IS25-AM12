package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;

public record ShipPieceRemoveEvent(String playerName, List<Point> positions) implements Event {
    public static ShipPieceRemoveEvent from(ShipBoard shipBoard, Point point, List<Point> positions) {
        return new ShipPieceRemoveEvent(
                Player.getPlayer(shipBoard).getNickname(),
                positions
        );
    }
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
