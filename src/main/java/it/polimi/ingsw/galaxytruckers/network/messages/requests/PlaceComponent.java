package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#placeComponent(Point, Direction)}
 */
public class PlaceComponent extends RegisteredRequest {
    private final Point point;
    private final Direction orientation;

    /**
     * Creates a new PlaceComponent request.
     *
     * @param point       the point where the component should be placed
     * @param orientation the orientation of the component
     */
    public PlaceComponent(Point point, Direction orientation) {
        this.point = point;
        this.orientation = orientation;
    }

    @Override
    public void execute(VirtualServer server) {
        server.placeComponent(point, orientation);
    }
}
