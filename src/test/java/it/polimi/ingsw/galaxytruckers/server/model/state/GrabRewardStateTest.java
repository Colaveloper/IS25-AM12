package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.*;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import org.checkerframework.dataflow.qual.AssertMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    CountDownLatch latch;

    @BeforeEach
    void setup() throws IOException {
        ship1 = new SecondShipBoardForTesting(GameColor.BLUE);
        ship2 = new SecondShipBoardForTesting(GameColor.RED);
        rewardMethod = Mockito.mock(Runnable.class);
        testState = new GrabRewardState(ship1, rewardMethod);
        game = new GameStub(Level.SECOND);
        game.setDeck(new Deck(game) {
            /**
             * Returns the current card to be played.
             *
             * @return the deck's current card
             */
            @Override
            public AdventureCard getCurrentCard() {
                return new AdventureCard(game,Level.TEST,0) {
                    @Override
                    public AdventureState getNextState() {
                        return new AdventureStateStub();
                    }
                };
            }
        });
        latch = StateTransitionUtils.setupLatch(game);
        game.setCurrentState(testState);
    }

    @AssertMethod
    void assertNoTransition() {
        StateTransitionUtils.assertNoTransition(latch,game,testState);
    }

    @Test
    void getShipBoard() {
        assertEquals(ship1, testState.getShipBoard());
    }

    @Test
    void skipDoesNothingWhenExpired() {
        testState.expired = true;
        testState.skip(ship1);
        assertNoTransition();
    }

    @Test
    void skipDoesNothingWhenOutOfTurn() {
        testState.skip(ship2);
        assertNoTransition();
    }

    @Test
    void skipChangesState() {
        testState.skip(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void grabRewardThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testState.grabReward(ship2));
    }

    @Test
    void grabRewardRunsRewardMethodAndChangesAdventureState() throws IOException {
        game = new GameStub(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
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
        game = new GameStub(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
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