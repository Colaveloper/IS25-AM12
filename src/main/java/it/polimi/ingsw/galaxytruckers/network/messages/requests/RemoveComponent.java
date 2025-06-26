package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#removeComponent(Point)}
 */
public class RemoveComponent extends RegisteredRequest {
    private final Point point;

    /**
     * Creates a new RemoveComponent request.
     *
     * @param point the point where the component should be removed
     */
    public RemoveComponent(Point point) {
        this.point = point;
    }

    @Override
    public void execute(VirtualServer server) {
        server.removeComponent(point);
    }
}
