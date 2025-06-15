package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record GrabPlacedComponentEvent(String playerName) implements LobbyEvent{
    public static GrabPlacedComponentEvent from(ShipBoard shipBoard) {
        return new GrabPlacedComponentEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }
}
