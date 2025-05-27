package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.SecondDeck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RemoveGoodsStateTest {
    RemoveGoodsState testState;
    ShipBoard ship1;
    Game game;
    Deck deck;
    AdventureCard adventureCard;
    Map<GoodsType, Integer> testGoods;

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.BLUE);
        testGoods = new HashMap<>();
    }

    void setupGame() throws IOException {
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        adventureCard = new AdventureCard(game, Level.SECOND, 1) {
            @Override
            public AdventureState getNextState() {
                return new AdventureStateStub();
            }
        };
        deck = new SecondDeck(game){
            @Override
            public AdventureCard getCurrentCard(){
                return adventureCard;
            }
        };
    }

    @Test
    void setGameChangesStateWhenNoGoodsToLose() throws IOException {
        setupGame();
        testState = new RemoveGoodsState(0, ship1);
        testState.setGame(game);
        assertNotEquals(testState, game.getCurrentState());
    }

    @Test
    void setGameDoesNotChangeStateWhenShipHasNoGoodsValue() throws IOException {
        ship1 = new SecondShipBoard(GameColor.BLUE){
            @Override
            public int getGoodsValue(){
                return 0;
            }
            @Override
            public int getNumBatteries(){
                return 1;
            }
        };
        setupGame();
        testState = new RemoveGoodsState(1, ship1);
        testState.setGame(game);
        assertNull(game.getCurrentState());
    }

    @Test
    void setGameDoesNotChangeStateWhenShipHasPositiveGoodsValue() throws IOException {
        ship1 = new SecondShipBoard(GameColor.BLUE){
            @Override
            public int getGoodsValue(){
                return 1;
            }
            @Override
            public int getNumBatteries(){
                return 1;
            }
        };
        setupGame();
        testState = new RemoveGoodsState(1, ship1);
        testState.setGame(game);
        assertNull(game.getCurrentState());
    }

    @Test
    void setGameChangesStateWhenShipHasBatteries() throws IOException {
        ship1 = new SecondShipBoard(GameColor.BLUE){
            @Override
            public int getGoodsValue(){
                return 0;
            }
            @Override
            public int getNumBatteries(){
                return 0;
            }
        };
        setupGame();
        testState = new RemoveGoodsState(1, ship1);
        testState.setGame(game);
        assertNotEquals(testState, game.getCurrentState());
    }

    @Test
    void loseGoodThrowsExceptionWhenOutOfTurn(){
        ShipBoard ship2 = new SecondShipBoard(GameColor.RED);
        testState = new RemoveGoodsState(2, ship2);
        assertThrows(IllegalStateException.class, () -> testState.loseGood(ship1, new Point(7,7)));
    }

    @Test
    void loseGoodUsesBatteriesIfNoMostValuableGoodPresentAndAttemptsStateTransition(){
        game = new Game(Level.SECOND);
        ship1 = new SecondShipBoard(GameColor.RED){
            @Override
            public Map<GoodsType, Integer> getGoods(){
                return testGoods;
            }
            @Override
            public void useBatteries(Point pos, int num){
                // mock
            }
            @Override
            public int getNumBatteries(){
                return 1;
            }
        };
        testState = new RemoveGoodsState(2,ship1);
        testState.loseGood(ship1, new Point(7,7));
        assertEquals(1, testState.goodsToLose);
        assertNull(game.getCurrentState());
    }

    @Test
    void loseGoodRemovesMostValuableGoodAndAttemptsStateTransition() throws IOException {
        testGoods.put(GoodsType.GREEN, 1);
        testGoods.put(GoodsType.RED, 1);
        testGoods.put(GoodsType.BLUE, 1);
        testGoods.put(GoodsType.YELLOW, 1);

        setupGame();

        ship1 = new SecondShipBoard(GameColor.BLUE){
            @Override
            public Map<GoodsType, Integer> getGoods(){
                return testGoods;
            }
            @Override
            public void removeGoods(Point pos, GoodsType good, int num){
                // mock
            }
        };
        testState = new RemoveGoodsState(2,ship1);
        testState.setGame(game);
        testState.loseGood(ship1, new Point(7,7));
        assertEquals(1, testState.goodsToLose);
        assertNotEquals(testState, game.getCurrentState());
    }

    @Test
    void loseGoodWithNoMostValuableGoodAndNoStateTransition() throws IOException {
        testGoods.put(GoodsType.GREEN, 0);

        setupGame();

        ship1 = new SecondShipBoard(GameColor.BLUE){
            @Override
            public Map<GoodsType, Integer> getGoods(){
                return testGoods;
            }
            @Override
            public void removeGoods(Point pos, GoodsType good, int num){
                // mock
            }
            @Override
            public void useBatteries(Point pos, int num){
                // mock
            }
            @Override
            public int getNumBatteries(){
                return 1;
            }
        };
        testState = new RemoveGoodsState(2,ship1);
        testState.setGame(game);
        testState.loseGood(ship1, new Point(7,7));
        assertEquals(1, testState.goodsToLose);
        assertNull(game.getCurrentState());
    }
}