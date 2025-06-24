package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.checkerframework.dataflow.qual.AssertMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class RemoveCrewStateTest {
    RemoveCrewState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Game game;
    Deck deck;
    AdventureCard adventureCard;
    CountDownLatch latch;

    @BeforeEach
    void setup() throws IOException {
        ship1 = new SecondShipBoardForTesting(GameColor.RED);
        ship1.initializeCabin(new Point(7,7), CrewType.HUMAN);
        game = new GameStub(Level.SECOND);
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
        game.setDeck(deck);
        latch = StateTransitionUtils.setupLatch(game);
        testState = new RemoveCrewState(2, ship1);
        game.setCurrentState(testState);
    }

    @AssertMethod
    void assertTransition() {
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @AssertMethod
    void assertNoTransition() {
        StateTransitionUtils.assertNoTransition(latch,game,testState);
    }

    @Test
    void skipDoesNothingWhenExpired() {
        testState.expired = true;
        testState.skip(ship1);
        assertNoTransition();
    }

    @Test
    void skipDoesNothingWhenOutOfTurn() {
        assertNotEquals(ship2,testState.getShipBoard());
        testState.skip(ship2);
        assertNoTransition();
    }

    @Test
    void skipChangesState() {
        testState.skip(ship1);
        assertTransition();
        assertEquals(0, ship1.getCrewSize());
    }

    @Test
    void skipChangesStateIfThereAreNoMoreCabins() {
        ship1.loseCrew(new Point(7,7));
        testState.skip(ship1);
        assertTransition();
        assertEquals(0, ship1.getCrewSize());
    }

    @Test
    void skipChangesStateIfThereIsNotEnoughCrew() {
        ship1.loseCrew(new Point(7,7));
        ship1.offerComponent(new Cabin());
        ship1.placeComponent(new Point(6,7), Direction.UP);
        ship1.weldLastComponent();
        testState.skip(ship1);
        assertTransition();
        assertEquals(0, ship1.getCrewSize());
    }

    @Test
    void loseCrewThrowsExceptionWhenOutOfTurn(){
        ship2 = new SecondShipBoardForTesting(GameColor.BLUE);
        assertNotEquals(ship2,testState.getShipBoard());
        assertThrows(IllegalStateException.class, () -> testState.loseCrew(ship2, new Point(7,7)));
    }

    @Test
    void loseCrewSacrificesCrew(){
        testState.loseCrew(ship1, new Point(7,7));
        assertEquals(1, testState.getCrewSacrifice());
    }

    @Test
    void loseCrewChangesAdventureStateWhenFinished() throws IOException {
        testState = new RemoveCrewState(1, ship1);
        game = new GameStub(Level.SECOND){
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
        game = new GameStub(Level.SECOND);
        CountDownLatch latch = StateTransitionUtils.setupLatch(game);
        testState.setGame(game);
        game.setDeck(deck);
        testState.loseCrew(ship1, new Point(7,7));
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void loseCrewChangesAdventureStateWhenNoMoreCrew() {
        ship1.loseCrew(new Point(7,7));
        testState.loseCrew(ship1, new Point(7,7));
        assertTransition();
    }

    @Test
    void loseCrewChangesAdventureStateWhenShipHasNoCrew() throws IOException{
        ship1 = new SecondShipBoardForTesting(GameColor.RED){
            @Override
            public int getCrewSize(){
                return 0;
            }
            @Override
            public void loseCrew(Point pos){
                // mock
            }
        };
        testState = new RemoveCrewState(2, ship1);
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
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void loseCrewDoesNotSacrificeCrewIfNoCrewToSacrificeAndChangesAdventureState() throws IOException{
        testState = new RemoveCrewState(0, ship1);
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
        testState.loseCrew(ship1, new Point(7,7));
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }
}