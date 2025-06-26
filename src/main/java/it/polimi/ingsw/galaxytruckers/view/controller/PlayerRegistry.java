package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.view.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for managing Player objects by nickname.
 * <p>
 * This class provides thread-safe methods to add, retrieve, and remove players by their nickname.
 * It is used to keep track of all players in the client or server context.
 * </p>
 */
public class PlayerRegistry {
    private final Map<String, Player> players = new HashMap<>();

    /**
     * Retrieves a Player by their nickname.
     *
     * @param nickname the nickname of the player
     * @return the Player object, or null if not found
     */
    public synchronized Player getByNickname(String nickname) {
        Player player;
        synchronized (players) {
            player = players.get(nickname);
        }
        return player;
    }

    /**
     * Adds a Player with the given nickname if not already present.
     *
     * @param nickname the nickname of the player
     * @return the existing or newly created Player object
     */
    public synchronized Player addPlayer(String nickname) {
        Player player;
        synchronized (players) {
            if (players.containsKey(nickname)) {
                player = players.get(nickname);
            } else {
                player = new Player(nickname);
                players.put(nickname, player);
            }
        }
        return player;
    }

    /**
     * Removes a Player from the registry.
     *
     * @param player the Player to remove
     */
    public synchronized void removePlayer(Player player) {
        synchronized (players) {
            players.remove(player.getNickname());
        }
    }

    /**
     * Clears all players from the registry.
     */
    public synchronized void clear() {
        synchronized (players) {
            players.clear();
        }
    }
}
