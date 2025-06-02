package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;

public class PlaceComponent extends RegisteredRequest {
    private final Point point;
    private final Direction orientation;

    public PlaceComponent(Point point, Direction orientation) {
        this.point = point;
        this.orientation = orientation;
    }

    @Override
    public void execute(VirtualServer server) {
        server.placeComponent(point, orientation);
    }
}
