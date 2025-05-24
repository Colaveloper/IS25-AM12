package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Set;

public record ValidateShipEvent(String playerName) implements Event{
    public static ValidateShipEvent from(ShipBoard shipBoard) {
        return new ValidateShipEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
