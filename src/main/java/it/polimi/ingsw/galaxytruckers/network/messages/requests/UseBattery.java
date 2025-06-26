package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#useBattery(Point)}
 */
public class UseBattery extends RegisteredRequest {
    private final Point position;

    /**
     * Constructor for UseBattery request.
     *
     * @param position the position on the ship where the battery is located
     */
    public UseBattery(Point position) {
        this.position = position;
    }

    @Override
    public void execute(VirtualServer server) {
        server.useBattery(position);
    }
}
