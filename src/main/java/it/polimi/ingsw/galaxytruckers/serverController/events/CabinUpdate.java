package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record CabinUpdate(String playerName, Point point, int numResidents, CrewType crewType) implements Event{
    public static CabinUpdate from(ShipBoard shipBoard, Point point, Cabin cabin) {
        return new CabinUpdate(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                cabin.getNumResidents(),
                cabin.getCrewType()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
