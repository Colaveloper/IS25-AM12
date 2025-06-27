package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.server.controller.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.Player;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages client sessions, allowing registration, unregistration, and cleanup of client handlers.
 * It also provides methods to check if a player is active and to ping a player to update their session.
 */
public class SessionManager {
    private static SessionManager instance;

    private final Map<Player, Session> activeSessions;
    private final ScheduledExecutorService scheduler;
    private ServerControllerInterface serverController;

    /**
     * Returns the singleton instance of SessionManager.
     * If the instance is null, it creates a new one.
     *
     * @return the singleton instance of SessionManager
     */
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

    /**
     * Sets the server controller for this session manager.
     * This controller is used to handle player disconnections.
     *
     * @param serverController the server controller to set
     */
    public void setServerController(ServerControllerInterface serverController) {
        this.serverController = serverController;
        this.scheduler.scheduleAtFixedRate(this::cleanup, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * Registers a client with the session manager.
     *
     * @param player the player associated with the client
     * @param client the player's client handler
     */
    public void registerClient(Player player, ClientHandler client) {
        synchronized (activeSessions) {
            activeSessions.put(player, new Session(client, Instant.now()));
        }
    }

    /**
     * Unregisters a client from the session manager.
     * If the client is found, it stops the associated client handler.
     *
     * @param player the player whose client is to be unregistered
     */
    public void unregisterClient(Player player) {
        Session session;
        synchronized (activeSessions) {
            session = activeSessions.remove(player);
        }
        if(session != null){
            session.getClientHandler().stop();
        }
    }

    /**
     * Clears all active sessions.
     */
    public void clear() {
        synchronized (activeSessions) {
            activeSessions.clear();
        }
    }

    /**
     * Returns the client handler associated with a player.
     *
     * @param player the player whose client handler is requested
     * @return the client handler for the player, or null if the player is not active
     */
    public ClientHandler getClient(Player player) {
        synchronized (activeSessions) {
            if (activeSessions.containsKey(player)) {
                return activeSessions.get(player).getClientHandler();
            } else {
                return null;
            }
        }
    }

    /**
     * Checks if a player is currently active in the session manager.
     *
     * @param player the player to check
     * @return true if the player is active, false otherwise
     */
    public boolean isPlayerActive(Player player) {
        synchronized (activeSessions) {
            return activeSessions.containsKey(player);
        }
    }

    /**
     * Updates the last ping time for a player, indicating that the player is still active.
     *
     * @param player the player to ping
     */
    public void ping(Player player) {
        synchronized (activeSessions) {
            if (activeSessions.containsKey(player)) {
                Session session = activeSessions.get(player);
                session.setLastPing(Instant.now());
            }
        }
    }

    /**
     * Shuts down the session manager, stopping the cleanup scheduler, charged
     * with checking for expired sessions.
     */
    public void shutDown() {
        scheduler.shutdown();
    }

    private void cleanup() {
        synchronized (activeSessions) {
            Instant now = Instant.now();
            Set<Player> activePlayers = new HashSet<>(activeSessions.keySet());
            for (Player player : activePlayers) {
                if (activeSessions.get(player).isExpired(now)) {
                    serverController.handlePlayerDisconnection(player);
                }
            }
        }
    }
}
