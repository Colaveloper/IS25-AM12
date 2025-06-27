package it.polimi.ingsw.galaxytruckers.client.model;

/**
 * Represents the high-level game states outside active gameplay in the Galaxy Truckers game.
 * This enum defines the various phases of user interaction with the game client,
 * from initial registration to game completion.
 */
public enum MetaState {
    /** Initial state where the player registers their username */
    REGISTER,

    /** State where the player chooses to join an existing lobby or create a new one */
    JOINORCREATE,

    /** State where the player is waiting in a lobby for the game to start */
    INLOBBY,

    /** State where the player is creating a new game lobby */
    CREATION,

    /** State where active gameplay is occurring, the adventure state of the game */
    INGAME,

    /** Final state after a game has concluded, showing results */
    ENDGAME
}
