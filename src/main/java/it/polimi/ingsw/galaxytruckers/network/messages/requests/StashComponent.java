package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#stashComponent()}
 */
public class StashComponent extends RegisteredRequest {
    @Override
    public void execute(VirtualServer server) {
        server.stashComponent();
    }
}
