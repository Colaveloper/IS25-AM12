package it.polimi.ingsw.galaxytruckers.network.messages;

import java.util.UUID;

/**
 * Represents a response message to a {@link Request}. It's used to
 * give feedback to the client about the success or failure of a request.
 */
public final class Response implements Message{
    private final UUID uuid;
    private final RuntimeException error;

    /**
     * Creates a Response with a given id and an exception.
     * @param uuid the unique identifier of the request this response is for
     * @param error the exception that occurred while processing the request
     */
    public Response(UUID uuid, RuntimeException error) {
        this.uuid = uuid;
        this.error = error;
    }

    /**
     * Creates a Response with a given id and no error, signaling a successful request.
     * @param uuid the unique identifier of the request this response is for
     */
    public Response(UUID uuid) {
        this.uuid = uuid;
        this.error = null;
    }

    /**
     * Gets the unique identifier of the request this response is for.
     * @return the UUID of the request
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Checks if the response indicates an error.
     * @return true if there is an error, false otherwise
     */
    public boolean isError() {
        return error != null;
    }

    /**
     * Gets the error associated with this response, if any.
     * @return the RuntimeException representing the error, or null if there is no error
     */
    public RuntimeException getError() {
        return error;
    }
}
