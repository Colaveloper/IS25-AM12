package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#acquireForecast(int)}
 */
public class AcquireForecast extends RegisteredRequest {
    private final int deckIndex;

    /**
     * Creates a new AcquireForecast request.
     * @param deckIndex the index of the deck to acquire the forecast from
     */
    public AcquireForecast(int deckIndex) {
        this.deckIndex = deckIndex;
    }

    @Override
    public void execute(VirtualServer server) {
        server.acquireForecast(deckIndex);
    }
}
