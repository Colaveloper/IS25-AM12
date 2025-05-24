package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;

import java.util.UUID;

public abstract non-sealed class Request implements Message{
    private final UUID uuid;

    protected Request(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public abstract void execute(VirtualServer server);
}
