package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.UUID;
import java.util.function.Function;

public interface ServerControllerInterface {

    void registerNickname(String nickname, ClientHandler handler);

    /**
     * Creates a new lobby for a game of the chosen level and with
     * the specified number of players, adding the creator to it
     *
     * @param creator    nickname of the lobby creator
     * @param level      level of the game
     * @param numPlayers number of players in the game
     * @return the {@link LobbyInterface} of the created lobby
     * @throws IllegalArgumentException if {@code numPlayers} is < 2
     */
    LobbyInterface newGame(Player creator, Level level, int numPlayers);

    /**
     * Adds the player with the specified nickname to the lobby
     * with the given lobby ID
     *
     * @param player  the nickname of the player joining a lobby
     * @param lobbyID the ID of the lobby the player wants to join
     * @return the {@link LobbyInterface} of the lobby the player has just entered
     * @throws IllegalArgumentException if there is no registered
     *                                  player with the given nickname or if there is no lobby with
     *                                  the given ID
     * @throws IllegalStateException    if the specified lobby is not
     *                                  in preparation phase
     */
    LobbyInterface joinLobby(Player player, UUID lobbyID);

    /**
     * Removes the given player from the active players in the server and, if
     * the player was in a lobby, notifies other players in the lobby of the
     * disconnection and interrupts the game
     * @param player the player who has disconnected
     */
    void handlePlayerDisconnection(Player player);

    /**
     * Notifies other players in the lobby of the player's exit and interrupts
     * the game
     * @param player the player who left
     */
    void leaveLobby(Player player);
}
