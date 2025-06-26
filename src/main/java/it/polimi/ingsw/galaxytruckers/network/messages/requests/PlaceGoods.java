package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#placeGoods(Point, GoodsType)}
 */
public class PlaceGoods extends RegisteredRequest {
    private final Point point;
    private final GoodsType goodsType;

    /**
     * Creates a new PlaceGoods request.
     *
     * @param point     the point where the goods should be placed
     * @param goodsType the type of goods to place
     */
    public PlaceGoods(Point point, GoodsType goodsType) {
        this.point = point;
        this.goodsType = goodsType;
    }

    @Override
    public void execute(VirtualServer server) {
        server.placeGoods(point, goodsType);
    }
}
