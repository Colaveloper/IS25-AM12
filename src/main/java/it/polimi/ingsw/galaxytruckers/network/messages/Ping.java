package it.polimi.ingsw.galaxytruckers.network.messages;

import java.util.UUID;

public record Ping(UUID id) implements Message{
    public Ping() {
        this(UUID.randomUUID());
    }
}
