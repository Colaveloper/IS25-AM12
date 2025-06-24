package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

public interface GameModelInterface {
    GameInterface createGame(Level level, int shipsN, GameEventListener gameEventListener);
}
