package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.SecondDeck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RemoveGoodsStateTest {
    RemoveGoodsState testState;
    ShipBoard ship1;
    Game game;
    Deck deck;
    AdventureCard adventureCard;

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.BLUE);
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
                return new AdventureState();
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
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
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
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
    }

    @Test
    void loseGoodThrowsExceptionWhenOutOfTurn(){
        ShipBoard ship2 = new SecondShipBoard(GameColor.RED);
        testState = new RemoveGoodsState(2, ship2);
        assertThrows(IllegalStateException.class, () -> testState.loseGood(ship1, new Point(7,7)));
    }

    @Test
    void loseGoodRemovesMostValuableGood(){
        // TODO: finish test
    }
}