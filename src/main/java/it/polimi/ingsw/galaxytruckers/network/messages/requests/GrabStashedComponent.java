package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#grabStashedComponent(int)}
 */
public class GrabStashedComponent extends RegisteredRequest {
    private final int index;

    /**
     * Constructor for GrabStashedComponent request.
     *
     * @param index the index of the stashed component to grab
     */
    public GrabStashedComponent(int index) {
        this.index = index;
    }

    @Override
    public void execute(VirtualServer server) {
        server.grabStashedComponent(index);
    }
}
