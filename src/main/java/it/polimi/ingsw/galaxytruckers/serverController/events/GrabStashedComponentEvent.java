package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;

public record GrabStashedComponentEvent(String playerName, int componentId, List<Integer> stashedComponentIds) implements Event {
    public static GrabStashedComponentEvent from(ShipBoard shipBoard, Component component) {
        return new GrabStashedComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                component.getId(),
                shipBoard.getStashedComponents().stream().map(Component::getId).toList()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
