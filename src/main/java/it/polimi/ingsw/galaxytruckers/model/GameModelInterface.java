package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

public interface GameModelInterface {
    /**
     * Creates a new game with the specified level, number of ships, and event listener.
     *
     * @param level the level of the game
     * @param shipsN the number of ships in the game
     * @param gameEventListener the listener for game events
     * @return a GameInterface instance representing the created game
     */
    GameInterface createGame(Level level, int shipsN, GameEventListener gameEventListener);
}
