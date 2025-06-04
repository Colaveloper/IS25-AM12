package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record InitializeCabinEvent(String playerName, Point point, CrewType crewType) implements LobbyEvent {
    public static InitializeCabinEvent from(ShipBoard shipBoard, Point point, Cabin cabin) {
        return new InitializeCabinEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                cabin.getCrewType()
        );
    }

}
