package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#rejectComponent()}
 */
public class RejectComponent extends RegisteredRequest {

    @Override
    public void execute(VirtualServer server) {
        server.rejectComponent();
    }
}
