package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

public class ActivateComponent extends RegisteredRequest {
    private final Point point;

    public ActivateComponent(Point point) {
        this.point = point;
    }

    @Override
    public void execute(VirtualServer server) {
        server.activateComponent(point);
    }
}
