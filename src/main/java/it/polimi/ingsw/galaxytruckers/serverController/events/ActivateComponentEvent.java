package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record ActivateComponentEvent(String playerName, Point point) implements Event{
    public static ActivateComponentEvent from(ShipBoard shipBoard, Point point) {
        return new ActivateComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
