package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#requestNewGame(Level, int)}
 */
public class RequestNewGame extends RegisteredRequest {
    private final Level level;
    private final int playerN;

    /**
     * Constructor for RequestNewGame request.
     *
     * @param level   the level of the game to be created
     * @param playerN the number of players in the game
     */
    public RequestNewGame(Level level, int playerN) {
        this.level = level;
        this.playerN = playerN;
    }

    @Override
    public void execute(VirtualServer server) {
        server.requestNewGame(level, playerN);
    }
}
