package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;

import java.util.UUID;

/**
 * Represents a request that can be sent to the server via sockets.
 * Each request has a unique identifier (UUID) and can be executed on a VirtualServer.
 */
public abstract sealed class Request implements Message permits RegisterNickname, RegisteredRequest {
    private final UUID uuid;

    /**
     * Creates a new Request with a unique identifier.
     */
    protected Request() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * @return the unique identifier of this request
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Executes this request on the given VirtualServer.
     *
     * @param server the VirtualServer on which to execute this request
     */
    public abstract void execute(VirtualServer server);
}
