package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel gameModel;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel();
    }

    @Test
    void createGame() {
        GameInterface gameInterface = gameModel.createGame(Level.SECOND, 2, new GameEventListenerForTesting());
        assertInstanceOf(Game.class, gameInterface);
        Game game = (Game) gameInterface;
        assertEquals(Level.SECOND, game.getLevel());
    }
}