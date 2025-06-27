package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#giveUp()}
 */
public class GiveUp extends RegisteredRequest {

    @Override
    public void execute(VirtualServer server) {
        server.giveUp();
    }
}
