package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class RequestComponent extends RegisteredRequest {
    private final int componentId;

    public RequestComponent(int componentId) {
        this.componentId = componentId;
    }

    @Override
    public void execute(VirtualServer server) {
        server.requestComponent(componentId);
    }
}
