package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.UUID;
import java.util.function.Function;

public interface ServerControllerInterface {

    /**
     * Registers a player with the given nickname and associates it with the provided client handler.
     * Sends the player information about the active lobbies he can join.
     * If the player is already registered but not active, it reconnects the player and
     * generates a snapshot of the game they are in.
     *
     * @param nickname the nickname of the player to register
     * @param handler  the client handler associated with the player
     */
    void registerNickname(String nickname, ClientHandler handler);

    /**
     * Creates a new lobby for a game of the chosen level and with
     * the specified number of players, adding the creator to it
     *
     * @param creator    nickname of the lobby creator
     * @param level      level of the game
     * @param numPlayers number of players in the game
     * @return the {@link LobbyInterface} of the created lobby
     */
    LobbyInterface newGame(Player creator, Level level, int numPlayers);

    /**
     * Adds the player with the specified nickname to the lobby
     * with the given lobby ID
     *
     * @param player  the nickname of the player joining a lobby
     * @param lobbyID the ID of the lobby the player wants to join
     * @return the {@link LobbyInterface} of the lobby the player has just entered
     */
    LobbyInterface joinLobby(Player player, UUID lobbyID);

    /**
     * Removes the given player from the active players in the server and, if
     * the player was in a lobby, notifies other players in the lobby of the
     * disconnection, otherwise it removes the player from the registered players.
     *
     * @param player the player who has disconnected
     */
    void handlePlayerDisconnection(Player player);

    /**
     * Notifies other players in the lobby of the player's exit and interrupts
     * the game
     *
     * @param player the player who left
     */
    void leaveLobby(Player player);
}
