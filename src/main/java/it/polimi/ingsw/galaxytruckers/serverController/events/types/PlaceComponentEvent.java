package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record PlaceComponentEvent(String playerName, Point position, int rotation) implements LobbyEvent {
    public static PlaceComponentEvent from(ShipBoard shipBoard, Component component, Point position) {
        return new PlaceComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                position,
                component.getOrientation()
        );
    }

}
