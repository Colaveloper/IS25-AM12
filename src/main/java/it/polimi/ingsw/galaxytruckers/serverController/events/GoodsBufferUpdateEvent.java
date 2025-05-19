package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public record GoodsBufferUpdateEvent(String playerName, GoodsType goodsType) implements Event {
    public static GoodsBufferUpdateEvent from(ShipBoard shipBoard, GoodsType goodsType) {
        return new GoodsBufferUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                goodsType
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
