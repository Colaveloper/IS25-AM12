package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public record CargoHoldUpdateEvent(String playerName, Point point, Map<GoodsType, Integer> cargo) implements Event {
    public static CargoHoldUpdateEvent from(ShipBoard shipBoard, Point point, CargoHold cargoHold) {
        return new CargoHoldUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                new HashMap<>(cargoHold.getGoods())
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
