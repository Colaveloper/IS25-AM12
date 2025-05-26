package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

public class UseBattery extends RegisteredRequest {
    private final Point position;

    public UseBattery(Point position) {
        this.position = position;
    }

    @Override
    public void execute(VirtualServer server) {
        server.useBattery(position);
    }
}
