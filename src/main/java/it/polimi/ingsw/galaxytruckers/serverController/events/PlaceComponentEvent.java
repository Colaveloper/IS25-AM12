package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;

public record PlaceComponentEvent(String playerName, Point position, Direction rotation) implements Event {
    public static PlaceComponentEvent from(ShipBoard shipBoard, Component component, Point position) {
        return new PlaceComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                position,
                component.getOrientation()
        );
    }

}
