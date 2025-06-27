package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#removeGoods(Point, GoodsType)}
 */
public class RemoveGoods extends RegisteredRequest {
    private final Point point;
    private final GoodsType goodsType;

    /**
     * Creates a new RemoveGoods request with the specified point and goods type.
     *
     * @param point     the point on the ship where the goods are located
     * @param goodsType the type of goods to remove
     */
    public RemoveGoods(Point point, GoodsType goodsType) {
        this.point = point;
        this.goodsType = goodsType;
    }

    @Override
    public void execute(VirtualServer server) {
        server.removeGoods(point, goodsType);
    }
}
