package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#requestComponent(int)}
 */
public class RequestComponent extends RegisteredRequest {
    private final int componentId;

    /**
     * Constructor for RequestComponent request.
     *
     * @param componentId the ID of the component to request
     */
    public RequestComponent(int componentId) {
        this.componentId = componentId;
    }

    @Override
    public void execute(VirtualServer server) {
        server.requestComponent(componentId);
    }
}
