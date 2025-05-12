package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public record ShipMapUpdateEvent(String playerName, Map<Point, Integer> componentIdMap) implements Event {
    public static ShipMapUpdateEvent from(ShipBoard shipBoard) {
        return new ShipMapUpdateEvent (
                Player.getPlayer(shipBoard).getNickname(),
                shipBoard.getComponentMap().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getId()))
                );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
