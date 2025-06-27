package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.util.UUID;

/**
 * Request class for {@link VirtualServer#joinLobby(UUID)}
 */
public class JoinLobby extends RegisteredRequest {
    private final UUID lobbyID;

    /**
     * Constructor for JoinLobby request.
     *
     * @param lobbyID the ID of the lobby to join
     */
    public JoinLobby(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    @Override
    public void execute(VirtualServer server) {
        server.joinLobby(lobbyID);
    }
}
