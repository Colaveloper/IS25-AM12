package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;
import java.util.Set;

public record ShipNotConnectedEvent(String playerName, List<Set<Point>> shipPieces) implements Event {
    public static ShipNotConnectedEvent from(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        return new ShipNotConnectedEvent(
                Player.getPlayer(shipBoard).getNickname(),
                shipPieces
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
