package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;

public record PlanetChoiceEvent(String playerName, int planetId, List<Point> cargoPoints) implements Event {
    public static PlanetChoiceEvent from(ShipBoard shipBoard, int planetId) {
        return new PlanetChoiceEvent(
                Player.getPlayer(shipBoard).getNickname(),
                planetId,
                shipBoard.getCargoHolds().keySet().stream().toList());
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
