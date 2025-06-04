package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record PlanetChoiceEvent(String playerName, int planetId) implements LobbyEvent {
    public static PlanetChoiceEvent from(ShipBoard shipBoard, int planetId) {
        return new PlanetChoiceEvent(
                Player.getPlayer(shipBoard).getNickname(),
                planetId
        );
    }

}
