package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record PlaceComponentEvent(String playerName, int componentId, int rotation, Point position) implements Event {
    public static PlaceComponentEvent from(ShipBoard shipBoard, Component component, Point position) {
        return new PlaceComponentEvent(
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
