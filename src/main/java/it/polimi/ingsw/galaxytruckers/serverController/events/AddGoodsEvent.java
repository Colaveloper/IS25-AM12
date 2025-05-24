package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;
import java.util.Map;

public record AddGoodsEvent(String playerName, Map<GoodsType, Integer> goods, List<Point> cargos) implements Event{
    public static AddGoodsEvent from(ShipBoard shipBoard, Map<GoodsType, Integer> goods, List<Point> cargos) {
        return new AddGoodsEvent(
                Player.getPlayer(shipBoard).getNickname(),
                goods,
                cargos
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
