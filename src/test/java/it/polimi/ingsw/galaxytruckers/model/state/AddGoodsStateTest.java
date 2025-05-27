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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AddGoodsStateTest {
    AddGoodsState testAddGoodState;
    ShipBoard ship1;
    ShipBoard ship2;
    Map<GoodsType, Integer> goodsBuffer;
    Game game;
    AdventureCard adventureCard;
    Deck deck;

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.RED){
            @Override
            public void placeGoods(Point pos, GoodsType goods, int num){
                // mock
            }
            @Override
            public void removeGoods(Point pos, GoodsType goods, int num){
                // mock
            }
        };
        ship2 = new SecondShipBoard(GameColor.BLUE);
        goodsBuffer = new HashMap<>();
        goodsBuffer.put(GoodsType.GREEN, 0);
        goodsBuffer.put(GoodsType.RED, 1);
        testAddGoodState = new AddGoodsState(goodsBuffer, ship1);
        game = new Game(Level.SECOND);
        game.setEventListener(new GameEventListenerStub());
        testAddGoodState.setGame(game);
    }

    @Test
    void addGoodThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testAddGoodState.addGood(ship2, new Point(7,7), GoodsType.GREEN));
    }

    @Test
    void addGoodThrowsExceptionWhenNoValidGoodTypeIsGiven(){
        assertThrows(IllegalArgumentException.class, () -> testAddGoodState.addGood(ship1, new Point(7,7), GoodsType.YELLOW));
    }

    @Test
    void addGoodThrowsExceptionWhenGoodAmountIsZero(){
        assertThrows(IllegalArgumentException.class, () -> testAddGoodState.addGood(ship1, new Point(7,7), GoodsType.GREEN));
    }

    @Test
    void addGoodPlacesGoodOnShip(){
        testAddGoodState.addGood(ship1, new Point(7,7), GoodsType.RED);
        assertEquals(0, testAddGoodState.getGoodsBuffer().get(GoodsType.RED));
    }

    @Test
    void removeGoodThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testAddGoodState.removeGood(ship2, new Point(7,7), GoodsType.GREEN));
    }

    @Test
    void removeGoodAddsToGoodsBufferIfKeyIsPresent(){
        testAddGoodState.removeGood(ship1, new Point(7,7), GoodsType.GREEN);
        assertEquals(1, testAddGoodState.getGoodsBuffer().get(GoodsType.GREEN));
    }

    @Test
    void removeGoodsAddsNewKeyToGoodsBufferIfNotAlreadyPresent(){
        testAddGoodState.removeGood(ship1, new Point(7,7), GoodsType.BLUE);
        assertEquals(1, testAddGoodState.getGoodsBuffer().get(GoodsType.BLUE));
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testAddGoodState.goNext(ship2));
    }

    @Test
    void goNextChangesGameState() throws IOException {
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
        testAddGoodState.setGame(game);
        testAddGoodState.goNext(ship1);
        assertNotEquals(testAddGoodState, game.getCurrentState());
    }

}