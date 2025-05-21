package it.polimi.ingsw.galaxytruckers.network.server;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Session {
    private static final Duration expirationDelay = Duration.of(15, ChronoUnit.SECONDS);
    private VirtualClient virtualClient;
    private Instant lastPing;

    public Session(VirtualClient virtualClient, Instant lastPing) {
        this.virtualClient = virtualClient;
        this.lastPing = lastPing;
    }

    public VirtualClient getVirtualClient() {
        return virtualClient;
    }

    public void setVirtualClient(VirtualClient virtualClient) {
        this.virtualClient = virtualClient;
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
