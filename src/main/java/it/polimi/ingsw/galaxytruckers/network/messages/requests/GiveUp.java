package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class GiveUp extends RegisteredRequest {

    @Override
    public void execute(VirtualServer server) {
        server.giveUp();
    }
}
