package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#activateComponent(Point)}
 */
public class ActivateComponent extends RegisteredRequest {
    private final Point point;

    /**
     * Constructor for ActivateComponent request.
     *
     * @param point the point on the ship where the component is located
     */
    public ActivateComponent(Point point) {
        this.point = point;
    }

    @Override
    public void execute(VirtualServer server) {
        server.activateComponent(point);
    }
}
