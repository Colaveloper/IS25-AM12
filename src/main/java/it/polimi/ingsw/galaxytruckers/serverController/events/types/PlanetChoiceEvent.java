package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record PlanetChoiceEvent(String playerName, int planetId, String nextPlayerName) implements LobbyEvent {
    public static PlanetChoiceEvent from(ShipBoard shipBoard, int planetId, ShipBoard nextShipBoard) {
        return new PlanetChoiceEvent(
                Player.getPlayer(shipBoard).getNickname(),
                planetId,
                Player.getPlayer(nextShipBoard).getNickname()
        );
    }
}
