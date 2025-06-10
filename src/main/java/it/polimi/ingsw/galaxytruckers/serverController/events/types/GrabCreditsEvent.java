package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record GrabCreditsEvent(String playerName, int value) implements LobbyEvent {
    public static GrabCreditsEvent from(ShipBoard shipBoard, int value) {
        return new GrabCreditsEvent(
                Player.getPlayer(shipBoard).getNickname(),
                value
        );
    }
}
