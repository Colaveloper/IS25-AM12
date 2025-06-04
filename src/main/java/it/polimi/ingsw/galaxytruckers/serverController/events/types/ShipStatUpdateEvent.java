package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record ShipStatUpdateEvent(String playerName, StatType statType, int value) implements LobbyEvent {
    public static ShipStatUpdateEvent from(ShipBoard shipBoard,  StatType statType, int value) {
        return new ShipStatUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                statType,
                value
        );
    }

}
