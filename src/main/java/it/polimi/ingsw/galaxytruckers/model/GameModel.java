package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

public class GameModel implements GameModelInterface {
    @Override
    public GameInterface createGame(Level level, int shipsN, GameEventListener gameEventListener) {
        return new Game(level, shipsN, gameEventListener);
    }
}
