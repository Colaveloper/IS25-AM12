package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
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
        GameInterface gameInterface = gameModel.createGame(Level.SECOND, 2);
        assertInstanceOf(Game.class, gameInterface);
        Game game = (Game) gameInterface;
        assertEquals(Level.SECOND, game.getLevel());
    }
}