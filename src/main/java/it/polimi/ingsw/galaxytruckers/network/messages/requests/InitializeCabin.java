package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.awt.*;

/**
 * Request class for {@link VirtualServer#initializeCabin(Point, CrewType)}
 */
public class InitializeCabin extends RegisteredRequest {
    private final Point point;
    private final CrewType crewType;

    /**
     * Constructor for InitializeCabin request.
     *
     * @param point    the point where the cabin is to be initialized
     * @param crewType the type of crew to be initialized in the cabin
     */
    public InitializeCabin(Point point, CrewType crewType) {
        this.point = point;
        this.crewType = crewType;
    }

    @Override
    public void execute(VirtualServer server) {
        server.initializeCabin(point, crewType);
    }
}
