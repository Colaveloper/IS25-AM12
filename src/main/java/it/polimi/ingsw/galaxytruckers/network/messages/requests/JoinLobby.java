package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

import java.util.UUID;

public class JoinLobby extends RegisteredRequest {
    private final UUID lobbyID;

    public JoinLobby(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    @Override
    public void execute(VirtualServer server) {
        server.joinLobby(lobbyID);
    }
}
