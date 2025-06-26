package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#choosePlanet(int)}
 */
public class ChoosePlanet extends RegisteredRequest {
    private final int choice;

    /**
     * Constructor for ChoosePlanet request.
     *
     * @param choice the index of the planet to choose, starting from 0
     */
    public ChoosePlanet(int choice) {
        this.choice = choice;
    }

    @Override
    public void execute(VirtualServer server) {
        server.choosePlanet(choice);
    }
}
