package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class AcquireForecast extends RegisteredRequest {
    private final int deckIndex;

    public AcquireForecast(int deckIndex) {
        this.deckIndex = deckIndex;
    }

    @Override
    public void execute(VirtualServer server) {
        server.acquireForecast(deckIndex);
    }
}
