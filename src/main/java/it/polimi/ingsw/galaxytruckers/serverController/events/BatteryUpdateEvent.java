package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Battery;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record BatteryUpdateEvent(String playerName, Point point, int numBatteries) implements Event{
    public static BatteryUpdateEvent from(ShipBoard shipBoard, Point point, Battery battery) {
        return new BatteryUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                battery.getNumBatteries()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
