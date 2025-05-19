package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.factory.SecondFactory;
import it.polimi.ingsw.galaxytruckers.model.factory.TestFactory;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GameTest {
    Game game;
    List<ShipBoard> shipBoards;

    @BeforeEach
    void setUp() {
        game = new Game(Level.SECOND);
        shipBoards = new ArrayList<>();
        shipBoards.add(game.addShipBoard(FourColors.BLUE));
        shipBoards.add(game.addShipBoard(FourColors.GREEN));
        game.setEventListener(new GameEventListenerStub());
    }

    @Test
    void gameIsCreatedCorrectly() {
        assertInstanceOf(SecondFactory.class, game.getGameFactory());
        game = new Game(Level.TEST);
        assertInstanceOf(TestFactory.class, game.getGameFactory());
    }

    @Test
    void gameIsNotCreatedIfLevelIsNotValid() {
        assertThrows(IllegalArgumentException.class, () -> new Game(Level.FIRST));
    }

    @Test
    void gameStartsCorrectly() {
        try {
            game.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertInstanceOf(ShipBuildingState.class,  game.getCurrentState());
        assertNotNull(game.getFlightBoard());
        assertNotNull(game.getDeck());
        assertTrue(game.getFlightBoard().getAllShips().containsAll(shipBoards));
        assertEquals(shipBoards.size(), game.getFlightBoard().getAllShips().size());
    }

    @Test
    void shipBoardsAreAddedCorrectly() {
        assertEquals(2, game.getShipBoards().size());
        assertTrue(game.getShipBoards().containsAll(shipBoards));
    }
}