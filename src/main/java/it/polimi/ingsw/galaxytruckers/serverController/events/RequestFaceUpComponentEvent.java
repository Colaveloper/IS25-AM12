package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Event signaling a player has requested a component from the bank
 * @param playerName the nickname of the player who performed the request
 * @param componentId the requested component's id
 * @param faceUpComponentIds the ids of face up components in the bank
 */
public record RequestFaceUpComponentEvent(String playerName, int componentId, Set<Integer> faceUpComponentIds) implements Event {

    public static RequestFaceUpComponentEvent from(ShipBoard shipBoard, Component component, ComponentBank componentBank) {
        return new RequestFaceUpComponentEvent(
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
