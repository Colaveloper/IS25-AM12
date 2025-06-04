package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record ShipPieceRemoveEvent(String playerName, int index) implements LobbyEvent {
    public static ShipPieceRemoveEvent from(ShipBoard shipBoard, int index) {
        return new ShipPieceRemoveEvent (
                Player.getPlayer(shipBoard).getNickname(),
                index
        );
    }
}
