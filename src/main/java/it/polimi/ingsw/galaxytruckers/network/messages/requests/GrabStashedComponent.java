package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class GrabStashedComponent extends RegisteredRequest {
    private final int index;

    public GrabStashedComponent(int index) {
        this.index = index;
    }

    @Override
    public void execute(VirtualServer server) {
        server.grabStashedComponent(index);
    }
}
