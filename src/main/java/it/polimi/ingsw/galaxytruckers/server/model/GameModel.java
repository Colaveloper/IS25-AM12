package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

public class GameModel implements GameModelInterface {
    @Override
    public GameInterface createGame(Level level, int shipsN, GameEventListener gameEventListener) {
        return new Game(level, shipsN, gameEventListener);
    }
}
