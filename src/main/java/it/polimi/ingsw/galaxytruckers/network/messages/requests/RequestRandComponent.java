package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#requestRandComponent()}
 */
public class RequestRandComponent extends RegisteredRequest {

    @Override
    public void execute(VirtualServer server) {
        server.requestRandComponent();
    }
}
