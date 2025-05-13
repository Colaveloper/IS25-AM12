package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static SessionManager instance;

    private final Map<Player, VirtualClient> activeSessions;

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    private SessionManager() {
        this.activeSessions = new HashMap<>();
    }

    public void registerClient(Player player, VirtualClient client) {
        activeSessions.put(player, client);
    }

    public void unregisterClient(Player player) {
        activeSessions.remove(player);
    }

    public VirtualClient getClient(Player player) {
        return activeSessions.get(player);
    }
}
