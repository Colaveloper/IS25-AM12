package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

public class RemoveGoods extends RegisteredRequest {
    private final Point point;
    private final GoodsType goodsType;

    public RemoveGoods(Point point, GoodsType goodsType) {
        this.point = point;
        this.goodsType = goodsType;
    }

    @Override
    public void execute(VirtualServer server) {
        server.removeGoods(point, goodsType);
    }
}
