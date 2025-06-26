package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#loseCrew(Point)}
 */
public class LoseCrew extends RegisteredRequest {
    private final Point point;

    /**
     * Creates a new LoseCrew request.
     *
     * @param point the point where the cabin is located
     */
    public LoseCrew(Point point) {
        this.point = point;
    }

    @Override
    public void execute(VirtualServer server) {
        server.loseCrew(point);
    }
}
