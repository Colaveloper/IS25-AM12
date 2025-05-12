package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Battery;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record BatteryUpdate(String playerName, Point point, int numBatteries) implements Event{
    public static BatteryUpdate from(ShipBoard shipBoard, Point point, Battery battery) {
        return new BatteryUpdate(
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
