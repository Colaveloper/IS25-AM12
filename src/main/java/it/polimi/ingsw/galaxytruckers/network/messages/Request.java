package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;

import java.util.UUID;

public abstract sealed class Request implements Message permits RegisterNickname, RegisteredRequest {
    private final UUID uuid;

    protected Request() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public abstract void execute(VirtualServer server);
}
