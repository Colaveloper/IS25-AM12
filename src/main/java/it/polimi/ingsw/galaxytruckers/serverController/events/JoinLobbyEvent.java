package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * ModelEvent signaling a player has either joined or left a lobby
 *
 * @param playerName the nickname of the player who left/joined
 */
public record JoinLobbyEvent(String playerName) implements Event {

    public static JoinLobbyEvent from(Player player) {
        return new JoinLobbyEvent(
            player.getNickname()
        );
    }

}
