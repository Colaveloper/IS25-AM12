package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.SecondDeck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.checkerframework.dataflow.qual.AssertMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class RemoveGoodsStateTest {
    CountDownLatch latch;
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
        game.setEventListener(new GameEventListenerStub());
        latch = StateTransitionUtils.setupLatch(game);
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

    @AssertMethod
    void assertNoTransition() {
        StateTransitionUtils.assertNoTransition(latch,game,testState);
    }

    @AssertMethod
    void assertTransition() {
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void setGameChangesStateWhenNoGoodsToLose() throws IOException, InterruptedException {
        setupGame();
        testState = new RemoveGoodsState(0, ship1);
        game.setCurrentState(testState);
        assertTransition();
    }

    @Test
    void setGameDoesNotChangeStateWhenShipHasNoGoodsValue() throws IOException, InterruptedException {
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
        game.setCurrentState(testState);
        assertNoTransition();
    }

    @Test
    void setGameDoesNotChangeStateWhenShipHasPositiveGoodsValue() throws IOException, InterruptedException {
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
        game.setCurrentState(testState);
        assertNoTransition();
    }

    @Test
    void setGameChangesStateWhenShipHasRunOutOfBatteries() throws IOException, InterruptedException {
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
        assertTransition();
    }

    @Test
    void loseGoodThrowsExceptionWhenOutOfTurn(){
        ShipBoard ship2 = new SecondShipBoard(GameColor.RED);
        testState = new RemoveGoodsState(2, ship2);
        assertThrows(IllegalStateException.class, () -> testState.loseGood(ship1, new Point(7,7)));
    }

    @Test
    void loseGoodUsesBatteriesIfNoMostValuableGoodPresentAndAttemptsStateTransition() throws InterruptedException {
        game = new Game(Level.SECOND);
        ship1 = new SecondShipBoard(GameColor.RED){
            @Override
            public Map<GoodsType, Integer> getGoods(){
                return testGoods;
            }
            @Override
            public void useBatteries(Point pos){
                // mock
            }
            @Override
            public int getNumBatteries(){
                return 1;
            }
        };
        testState = new RemoveGoodsState(2,ship1);
        game.setEventListener(new GameEventListenerStub());
        latch = StateTransitionUtils.setupLatch(game);
        game.setCurrentState(testState);
        testState.loseGood(ship1, new Point(7,7));
        assertEquals(1, testState.goodsToLose);
        assertNoTransition();
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
            public int getGoodsValue() {
                return testGoods.keySet().stream()
                        .mapToInt(GoodsType::getValue).sum();
            }

            @Override
            public void removeGoods(Point pos, GoodsType good, int num){
                // mock
            }
        };
        testState = new RemoveGoodsState(2,ship1);
        game.setCurrentState(testState);
        game.setEventListener(new GameEventListenerStub());
        testState.loseGood(ship1, new Point(7,7));
        assertEquals(1, testState.goodsToLose);
        assertNoTransition();
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
            public void useBatteries(Point pos){
                // mock
            }
            @Override
            public int getNumBatteries(){
                return 1;
            }
        };
        testState = new RemoveGoodsState(2,ship1);
        game.setEventListener(new GameEventListenerStub());
        game.setCurrentState(testState);
        testState.loseGood(ship1, new Point(7,7));
        assertEquals(1, testState.goodsToLose);
        assertNoTransition();
    }
}