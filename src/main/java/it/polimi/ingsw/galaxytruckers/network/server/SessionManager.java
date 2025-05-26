package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.rmi.RemoteException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SessionManager {
    private static SessionManager instance;

    private final Map<Player, Session> activeSessions;
    private final ScheduledExecutorService scheduler;
    // TODO: consider reducing the interface to include handlePlayerDisconnection only
    private ServerControllerInterface serverController;

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    private SessionManager() {
        this.activeSessions = new HashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void setServerController(ServerControllerInterface serverController) {
        this.serverController = serverController;
        this.scheduler.scheduleAtFixedRate(this::cleanup, 5, 5, TimeUnit.SECONDS);
    }

    public void registerClient(Player player, VirtualClient client) {
        synchronized (activeSessions) {
            activeSessions.put(player, new Session(client, Instant.now()));
        }
    }

    public void unregisterClient(Player player) {
        synchronized (activeSessions) {
            activeSessions.remove(player);
        }
    }

    public VirtualClient getClient(Player player) {
        synchronized (activeSessions) {
            return activeSessions.get(player).getVirtualClient();
        }
    }

    public void ping(Player player) {
        Session session;
        synchronized (activeSessions) {
            if (activeSessions.containsKey(player)) {
                session = activeSessions.get(player);
            } else return;
        }
        session.setLastPing(Instant.now());
    }

    public void shutDown() {
        scheduler.shutdown();
    }

    private void cleanup() {
        List<Player> expiredPlayers = new ArrayList<>();
        Instant now = Instant.now();
        synchronized (activeSessions) {
            for (Player player : activeSessions.keySet()) {
                if (activeSessions.get(player).isExpired(now)) {
                    expiredPlayers.add(player);
                }
            }
        }
        for (Player player : expiredPlayers) {
            serverController.handlePlayerDisconnection(player);
        }
    }
}
