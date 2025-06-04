package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record ValidateShipEvent(String playerName) implements LobbyEvent {
    public static ValidateShipEvent from(ShipBoard shipBoard) {
        return new ValidateShipEvent(
                Player.getPlayer(shipBoard).getNickname()
        );
    }

}
