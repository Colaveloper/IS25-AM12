package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

public record GoodsUpdateEvent(String playerName, Point point, GoodsType goodsType, boolean add) implements LobbyEvent {
    public static GoodsUpdateEvent from(ShipBoard shipBoard, Point point, GoodsType goodsType, boolean add) {
        return new GoodsUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                goodsType,
                add
        );
    }

}
