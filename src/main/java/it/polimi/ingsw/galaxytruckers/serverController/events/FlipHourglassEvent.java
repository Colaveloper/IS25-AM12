package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record FlipHourglassEvent(String playerName) implements Event {

    public static FlipHourglassEvent from(ShipBoard shipBoard) {
        return new FlipHourglassEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
