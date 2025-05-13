package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public record ShipMapUpdateEvent(String playerName, int componentId, int rotation, Point position) implements Event {
    public static ShipMapUpdateEvent from(ShipBoard shipBoard, Component component, Point position) {
        return new ShipMapUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                component.getId(),
                component.getOrientation(),
                position
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
