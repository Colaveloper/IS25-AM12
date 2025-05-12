package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record FlipHourglassEvent(String playerName, boolean isLast) implements Event {

    public static FlipHourglassEvent from(ShipBoard shipBoard, boolean isLast) {
        return new FlipHourglassEvent(
                Player.getPlayer(shipBoard).getNickname(),
                isLast
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
