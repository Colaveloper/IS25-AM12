package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

public class GameStub extends Game {
    public GameStub(Level level) {
        super(level, 4, GameEventListenerForTesting.getMock());
    }
}
