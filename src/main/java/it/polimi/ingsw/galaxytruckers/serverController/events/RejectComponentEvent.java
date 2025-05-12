package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.HashSet;
import java.util.Set;

public record RejectComponentEvent(String playerName, int componentId, Set<Integer> faceUpComponentIds) implements Event {

    public static RejectComponentEvent from(ShipBoard shipBoard, Component component, ComponentBank componentBank) {
        return new RejectComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                component.getId(),
                new HashSet<>(componentBank.getUncoveredComponents().keySet())
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
