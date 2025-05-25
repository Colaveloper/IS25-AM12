package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;

public class RequestNewGame extends RegisteredRequest{
    private final Level level;
    private final int playerN;

    public RequestNewGame(Level level, int playerN) {
        this.level = level;
        this.playerN = playerN;
    }

    @Override
    public void execute(VirtualServer server) {
        server.requestNewGame(level, playerN);
    }
}
