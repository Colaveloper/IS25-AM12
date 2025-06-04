package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record RequestFaceDownComponentEvent(String playerName, int componentId) implements LobbyEvent {

    public static RequestFaceDownComponentEvent from(ShipBoard shipBoard, Component component) {
        return new RequestFaceDownComponentEvent(
                Player.getPlayer(shipBoard).getNickname(),
                component.getId()
        );
    }

}
