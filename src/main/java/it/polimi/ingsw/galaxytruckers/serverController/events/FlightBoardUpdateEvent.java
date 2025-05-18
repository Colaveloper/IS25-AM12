package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Map;
import java.util.stream.Collectors;

public record FlightBoardUpdateEvent(String playerName, int position) implements Event {
    public static FlightBoardUpdateEvent from(ShipBoard shipBoard, int position) {
        return new FlightBoardUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                position
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
