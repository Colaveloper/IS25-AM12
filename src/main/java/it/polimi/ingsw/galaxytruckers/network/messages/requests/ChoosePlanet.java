package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class ChoosePlanet extends RegisteredRequest {
    private final int choice;

    public ChoosePlanet(int choice) {
        this.choice = choice;
    }

    @Override
    public void execute(VirtualServer server) {
        server.choosePlanet(choice);
    }
}
