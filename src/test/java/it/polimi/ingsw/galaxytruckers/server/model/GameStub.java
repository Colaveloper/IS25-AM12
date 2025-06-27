package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

public class GameStub extends Game {
    public GameStub(Level level) {
        super(level, 4, GameEventListenerForTesting.getMock());
    }
}
