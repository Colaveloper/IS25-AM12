package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Event signaling a player has either joined or left a lobby
 * @param playerName the nickname of the player who left/joined
 * @param playerColors a map containing the nicknames of all players in the lobby
 *                     and their assigned colors
 */
public record LobbyEvent(String playerName, Map<String, Colors> playerColors) implements Event {

    public static LobbyEvent from(Player player, Lobby lobby) {
        return new LobbyEvent(
            player.getNickname(),
            lobby.getPlayers()
                    .stream()
                    .collect(Collectors.toMap(
                            Player::getNickname,
                            p -> p.getColor().orElseThrow(IllegalStateException::new)
                    ))
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    //TODO: add ColorChoiceEvent if we decide to leave choice to the player,
    // otherwise it can be derived from this event
}
