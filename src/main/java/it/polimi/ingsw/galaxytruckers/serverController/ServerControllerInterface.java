package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.UUID;

public interface ServerControllerInterface {
    /**
     * Creates a new player with the given nickname
     * @param nickname the nickname of the new player
     * @throws IllegalArgumentException if there is already a player
     * with the given nickname
     */
    Player registerNickname(String nickname);

    /**
     * Creates a new lobby for a game of the chosen level and with
     * the specified number of players, adding the creator to it
     *
     * @param creator    nickname of the lobby creator
     * @param level      level of the game
     * @param numPlayers number of players in the game
     * @return
     * @throws IllegalArgumentException if {@code numPlayers} is < 2
     */
    LobbyInterface newGame(Player creator, Level level, int numPlayers);

    /**
     * Adds the player with the specified nickname to the lobby
     * with the given lobby ID
     *
     * @param player  the nickname of the player joining a lobby
     * @param lobbyID the ID of the lobby the player wants to join
     * @return
     * @throws IllegalArgumentException if there is no registered
     *                                  player with the given nickname or if there is no lobby with
     *                                  the given ID
     * @throws IllegalStateException    if the specified lobby is not
     *                                  in preparation phase
     */
    LobbyInterface joinLobby(Player player, UUID lobbyID);

    /**
     * Removes the player with the given nickname from the lobby
     * they are currently in
     * @param nickname the nickname of the player wishing to leave
     *                 the lobby
     * @throws IllegalArgumentException if there is no registered
     * player with the given nickname
     * @throws IllegalStateException if the player has not joined
     * a lobby yet
     */
    void leaveLobby(String nickname);
}
