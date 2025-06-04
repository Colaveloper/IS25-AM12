package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * ModelEvent signaling a player has requested a component from the bank
 * @param playerName the nickname of the player who performed the request
 * @param componentId the requested component's id
 */
public record RequestFaceUpComponentEvent(String playerName, int componentId) implements LobbyEvent {

    public static RequestFaceUpComponentEvent from(ShipBoard shipBoard, Component component) {
        return new RequestFaceUpComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                component.getId()
        );
    }

}
