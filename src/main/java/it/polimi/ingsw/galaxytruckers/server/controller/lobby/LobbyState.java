package it.polimi.ingsw.galaxytruckers.server.controller.lobby;

/**
 * Enum representing the possible states of a game lobby.
 * Used to track and control the lifecycle of a lobby throughout the game.
 *  A lobby is either in preparation or in-game.
 */
public enum LobbyState {
    PREPARATION,
    INGAME
}
