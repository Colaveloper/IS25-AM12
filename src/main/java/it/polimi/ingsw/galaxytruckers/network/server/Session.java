package it.polimi.ingsw.galaxytruckers.network.server;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Represents a session for a client handler, which includes the last ping time
 * and provides methods to check if the session has expired.
 */
public class Session {
    private static final Duration expirationDelay = Duration.of(15, ChronoUnit.SECONDS);
    private final ClientHandler clientHandler;
    private Instant lastPing;

    /**
     * Creates a new session for the given client handler
     *
     * @param clientHandler the client handler associated with this session
     * @param lastPing      the instant of the last ping
     */
    public Session(ClientHandler clientHandler, Instant lastPing) {
        this.clientHandler = clientHandler;
        this.lastPing = lastPing;
    }

    /**
     * @return the client handler associated with this session
     */
    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    /**
     * Sets the last ping time for this session
     *
     * @param lastPing time of the last ping
     */
    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

    /**
     * Checks if the session has expired based on the last ping time
     *
     * @param now the current time to compare against the last ping
     * @return true if the session has expired, false otherwise
     */
    public boolean isExpired(Instant now) {
        return Duration.between(lastPing, now).compareTo(expirationDelay) >= 0;
    }
}
