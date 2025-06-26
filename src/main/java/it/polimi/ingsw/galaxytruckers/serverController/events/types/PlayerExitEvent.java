package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;

/**
 * Event representing a player exiting the lobby.
 *
 * @param playerName the name of the player who exited
 */
public record PlayerExitEvent(String playerName) implements LobbyEvent {
    /**
     * {@inheritDoc}
     * <p> This implementation removes the lobby</p>
     *
     * @param lobby the lobby on which to perform the action
     */
    @Override
    public void runLobbyAction(Lobby lobby) {
        lobby.remove();
    }
}
