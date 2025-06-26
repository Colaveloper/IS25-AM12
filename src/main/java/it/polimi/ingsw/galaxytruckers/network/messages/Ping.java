package it.polimi.ingsw.galaxytruckers.network.messages;

import java.util.UUID;

/**
 * Represents a ping message used to check the connection status sent by the
 * client to the server.
 * @param id the unique identifier for the ping message
 */
public record Ping(UUID id) implements Message{
    public Ping() {
        this(UUID.randomUUID());
    }
}
