package it.polimi.ingsw.galaxytruckers.network.server;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Session {
    private static final Duration expirationDelay = Duration.of(15, ChronoUnit.SECONDS);
    private ClientHandler clientHandler;
    private Instant lastPing;

    public Session(ClientHandler clientHandler, Instant lastPing) {
        this.clientHandler = clientHandler;
        this.lastPing = lastPing;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

    public boolean isExpired(Instant now) {
        return Duration.between(lastPing, now).compareTo(expirationDelay) >= 0;
    }
}
