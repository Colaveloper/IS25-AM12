package it.polimi.ingsw.galaxytruckers.network.messages;

import java.util.UUID;

public final class Response implements Message{
    private final UUID uuid;
    private final RuntimeException error;

    public Response(UUID uuid, RuntimeException error) {
        this.uuid = uuid;
        this.error = error;
    }

    public Response(UUID uuid) {
        this.uuid = uuid;
        this.error = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isError() {
        return error != null;
    }

    public RuntimeException getError() {
        return error;
    }
}
