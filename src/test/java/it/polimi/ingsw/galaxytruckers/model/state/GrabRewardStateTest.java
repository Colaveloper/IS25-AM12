package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.SecondDeck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class GrabRewardStateTest {
    GrabRewardState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Runnable rewardMethod;
    Game game;
    Deck deck;
    AdventureCard adventureCard;

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.BLUE);
        ship2 = new SecondShipBoard(GameColor.RED);
        rewardMethod = () ->{
            // mock
        };
        testState = new GrabRewardState(ship1, rewardMethod);
    }

    @Test
    void grabRewardThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testState.grabReward(ship2));
    }

    @Test
    void grabRewardRunsRewardMethodAndChangesAdventureState() throws IOException {
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        game.setEventListener(new GameEventListenerStub());
        CountDownLatch latch = StateTransitionUtils.setupLatch(game);
        adventureCard = new AdventureCard(game, Level.SECOND,1) {
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
        testState.setGame(game);
        testState.grabReward(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testState.goNext(ship2));
    }

    @Test
    void goNextChangesAdventureState() throws  IOException{
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        game.setEventListener(new GameEventListenerStub());
        CountDownLatch latch = StateTransitionUtils.setupLatch(game);
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
        testState.setGame(game);
        testState.goNext(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

}