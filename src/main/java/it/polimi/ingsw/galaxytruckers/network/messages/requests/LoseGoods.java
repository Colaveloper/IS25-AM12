package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#loseGoods(Point)}
 */
public class LoseGoods extends RegisteredRequest {
    private final Point point;

    /**
     * Creates a new LoseGoods request.
     *
     * @param point the point on the ship where goods are to be lost
     */
    public LoseGoods(Point point) {
        this.point = point;
    }

    @Override
    public void execute(VirtualServer server) {
        server.loseGoods(point);
    }
}
