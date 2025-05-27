package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EndGameStateTest {

    @Test
    void endGame(){
        Map<ShipBoard, Integer> finalScores = new HashMap<>();
        EndGameState testState = new EndGameState(finalScores);
        Game game = new Game(Level.SECOND);
        game.setEventListener(new GameEventListenerStub());
        testState.setGame(game);
        testState.endGame();
    }

}