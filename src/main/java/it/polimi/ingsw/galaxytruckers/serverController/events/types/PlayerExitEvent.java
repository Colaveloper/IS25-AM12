package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;

/**
 * Event that represents a player exiting the game.
 * This event is dispatched when a player leaves a game lobby, either voluntarily
 * or due to disconnection.
 *
 * @param playerName The name of the player who is exiting the game
 */
public record PlayerExitEvent(String playerName) implements LobbyEvent {
    /**
     * Executes the lobby action for this event.
     * Removes the lobby as a player has exited the game.
     *
     * @param lobby The lobby from which the player has exited
     */
    @Override
    public void runLobbyAction(Lobby lobby) {
        lobby.remove();
    }
}
