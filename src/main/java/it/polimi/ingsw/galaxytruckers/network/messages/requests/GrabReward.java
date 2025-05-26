package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class GrabReward extends RegisteredRequest {
    private final boolean grab;

    public GrabReward(boolean grab) {
        this.grab = grab;
    }

    @Override
    public void execute(VirtualServer server) {
        server.grabReward(grab);
    }
}
