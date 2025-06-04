package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record RejectComponentEvent(String playerName) implements LobbyEvent {

    public static RejectComponentEvent from(ShipBoard shipBoard, Component component) {
        return new RejectComponentEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
