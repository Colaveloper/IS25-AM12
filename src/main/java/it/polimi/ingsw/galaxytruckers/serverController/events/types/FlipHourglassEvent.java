package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record FlipHourglassEvent(String playerName) implements LobbyEvent {

    public static FlipHourglassEvent from(ShipBoard shipBoard) {
        return new FlipHourglassEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
