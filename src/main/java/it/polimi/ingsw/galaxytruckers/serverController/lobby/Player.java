package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a registered player in the whole application. Provides methods
 * to add, remove and retrieve players by their nickname. Also provides some
 * utilities to manage the association between players and their ship boards.
 */
public class Player {
    private final static Map<String, Player> nicknameToPlayer = new HashMap<>();
    private final static Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    private final String nickname;
    private final AtomicReference<Lobby> lobby = new AtomicReference<>();
    private final AtomicReference<ShipBoard> shipBoard = new AtomicReference<>();

    /**
     * Adds a new player to the application with the given nickname.
     *
     * @param nickname the nickname of the player to add
     * @return the newly created Player object
     * @throws IllegalArgumentException if a player with the same nickname already exists
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
     * Removes a player from the application by their nickname.
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
     * @return the Player object associated with the given nickname, or null if no such player exists
     */
    public static Player getPlayer(String nickname) {
        Player player;
        synchronized (nicknameToPlayer) {
            player = nicknameToPlayer.get(nickname);
        }
        return player;
    }

    /**
     * Retrieves all players currently registered in the application.
     *
     * @return a set of all Player objects
     */
    public static Set<Player> getAllPlayers() {
        Set<Player> res;
        synchronized (nicknameToPlayer) {
            res = new HashSet<>(nicknameToPlayer.values());
        }
        return res;
    }

    /**
     * Removes all players from the application. Should be used
     * for testing purposes only.
     */
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
     * Retrieves the player associated with a given ship board.
     *
     * @param shipBoard the ship board for which to retrieve the player
     * @return the Player object associated with the given ship board
     */
    public static Player getPlayer(ShipBoard shipBoard) {
        synchronized (shipToPlayer) {
            if (!shipToPlayer.containsKey(shipBoard)) {
                throw new IllegalArgumentException("There is no player for this ship board");
            }
            return shipToPlayer.get(shipBoard);
        }
    }

    private Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the lobby the player is currently in, if any
     */
    public Optional<Lobby> getLobby() {
        return Optional.ofNullable(this.lobby.get());
    }

    /**
     * @return the ship board of the player, if any
     */
    public Optional<ShipBoard> getShipBoard() {
        return Optional.ofNullable(this.shipBoard.get());
    }

    /**
     * Sets the lobby for this player. Used when the player is added
     * to a lobby.
     */
    public void setLobby(Lobby lobby) {
        this.lobby.set(lobby);
    }

    /**
     * Sets the ship board for this player. This method also
     * enables the retrieval of the player by their ship board using
     * {@link Player#getPlayer(ShipBoard)}.
     *
     * @param shipBoard the ship board to associate with this player
     */
    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard.set(shipBoard);
        synchronized (shipToPlayer) {
            shipToPlayer.put(shipBoard, this);
        }
    }

    /**
     * Removes the player's lobby and ship board association.
     */
    public void leaveLobby() {
        this.lobby.set(null);
        synchronized (shipToPlayer) {
            shipToPlayer.remove(shipBoard.get());
        }
        this.shipBoard.set(null);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player player)) return false;
        return Objects.equals(nickname, player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }
}
