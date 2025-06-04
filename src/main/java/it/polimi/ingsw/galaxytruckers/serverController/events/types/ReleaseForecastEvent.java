package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record ReleaseForecastEvent(String playerName, int deckIndex) implements LobbyEvent {
    public static ReleaseForecastEvent from(ShipBoard shipBoard, int deckIndex) {
        return new ReleaseForecastEvent(
                Player.getPlayer(shipBoard).getNickname(),
                deckIndex
        );
    }

}
