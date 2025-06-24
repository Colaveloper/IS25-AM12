package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a player in the Galaxy Truckers game.
 * Manages player identity, game associations, and provides lookup functionality
 * for players by nickname or ship board.
 * Each player has a unique nickname, and may be associated with a lobby, ship board, and color.
 */
public class Player {
    private final static Map<String, Player> nicknameToPlayer = new HashMap<>();
    private final static Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    private final String nickname;
    private final AtomicReference<Lobby> lobby = new AtomicReference<>();
    private final AtomicReference<ShipBoard> shipBoard = new AtomicReference<>();

    /**
     * Creates and registers a new player with the specified nickname.
     *
     * @param nickname the unique nickname for the player
     * @return the newly created Player instance
     * @throws IllegalArgumentException if the nickname already exists
     */
    public static Player addPlayer(String nickname) {
        Player player;
        synchronized (nicknameToPlayer) {
            if (nicknameToPlayer.containsKey(nickname)) {
                throw new IllegalArgumentException("Nickname already exists");
            }
            player = new Player(nickname);
            nicknameToPlayer.put(nickname, player);
        }
        return player;
    }

    /**
     * Removes a player from the global player registry.
     *
     * @param nickname the nickname of the player to remove
     */
    public static void removePlayer(String nickname) {
        synchronized (nicknameToPlayer) {
            nicknameToPlayer.remove(nickname);
        }
    }

    /**
     * Retrieves a player by their nickname.
     *
     * @param nickname the nickname of the player to retrieve
     * @return the Player instance, or null if no player with the given nickname exists
     */
    public static Player getPlayer(String nickname) {
        Player player;
        synchronized (nicknameToPlayer) {
            player = nicknameToPlayer.get(nickname);
        }
        return player;
    }

    /**
     * Gets a set of all registered players.
     *
     * @return a set containing all Player instances
     */
    public static Set<Player> getAllPlayers() {
        Set<Player> res;
        synchronized (nicknameToPlayer) {
            res = new HashSet<>(nicknameToPlayer.values());
        }
        return res;
    }

    @VisibleForTesting
    public static void clear() {
        synchronized (nicknameToPlayer) {
            nicknameToPlayer.clear();
        }
        synchronized (shipToPlayer) {
            shipToPlayer.clear();
        }
    }

    /**
     * Retrieves a player by their ship board.
     *
     * @param shipBoard the ship board associated with the player
     * @return the Player instance associated with the given ship board
     * @throws IllegalArgumentException if no player is associated with the ship board
     */
    public static Player getPlayer(ShipBoard shipBoard) {
        synchronized (shipToPlayer) {
            if (!shipToPlayer.containsKey(shipBoard)) {
                throw new IllegalArgumentException("There is no player for this ship board");
            }
            return shipToPlayer.get(shipBoard);
        }
    }

    /**
     * Creates a new Player with the specified nickname.
     *
     * @param nickname the unique nickname for this player
     */
    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the player's nickname.
     *
     * @return the nickname of this player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the lobby this player is currently in.
     *
     * @return an Optional containing the player's lobby, or empty if not in a lobby
     */
    public Optional<Lobby> getLobby() {
        return Optional.ofNullable(this.lobby.get());
    }

    /**
     * Gets the ship board associated with this player.
     *
     * @return an Optional containing the player's ship board, or empty if no ship board is assigned
     */
    public Optional<ShipBoard> getShipBoard() {
        return Optional.ofNullable(this.shipBoard.get());
    }

    public void setLobby(Lobby lobby) {
        this.lobby.set(lobby);
    }

    protected void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard.set(shipBoard);
        synchronized (shipToPlayer) {
            shipToPlayer.put(shipBoard, this);
        }
    }

    /**
     * Checks if this player is equal to another object.
     * Players are considered equal if they have the same nickname.
     *
     * @param o the object to compare with
     * @return true if the given object is a Player with the same nickname
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player player)) return false;
        return Objects.equals(nickname, player.nickname);
    }

    /**
     * Returns a hash code for this player based on the nickname.
     *
     * @return a hash code value for this player
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }

    /**
     * Removes this player from their current lobby and clears associated game elements.
     * This resets the player's lobby, ship board, and color.
     */
    public void leaveLobby() {
        this.lobby.set(null);
        synchronized (shipToPlayer) {
            shipToPlayer.remove(shipBoard.get());
        }
        this.shipBoard.set(null);
    }
}
