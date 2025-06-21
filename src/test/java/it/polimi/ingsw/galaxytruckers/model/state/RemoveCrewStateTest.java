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

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.RED){
            @Override
            public int getCrewSize(){
                return 1;
            }
            @Override
            public void loseCrew(Point pos){
                // mock
            }
        };
        game = new Game(Level.SECOND);
        game.setEventListener(new GameEventListenerStub());
        testState = new RemoveCrewState(2, ship1);
        testState.setGame(game);
    }

    @Test
    void loseCrewThrowsExceptionWhenOutOfTurn(){
        ship2 = new SecondShipBoard(GameColor.BLUE);
        assertThrows(IllegalStateException.class, () -> testState.loseCrew(ship2, new Point(7,7)));
    }

    @Test
    void loseCrewSacrificesCrew(){
        testState.loseCrew(ship1, new Point(7,7));
        assertEquals(1, testState.crewSacrifice);
    }

    @Test
    void loseCrewChangesAdventureState() throws IOException {
        testState = new RemoveCrewState(1, ship1);
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
        game = new Game(Level.SECOND);
        game.setEventListener(new GameEventListenerStub());
        CountDownLatch latch = StateTransitionUtils.setupLatch(game);
        testState.setGame(game);
        game.setDeck(deck);
        testState.loseCrew(ship1, new Point(7,7));
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void loseCrewChangesAdventureStateWhenShipHasNoCrew() throws IOException{
        ship1 = new SecondShipBoard(GameColor.RED){
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
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void loseCrewDoesNotSacrificeCrewIfNoCrewToSacrificeAndChangesAdventureState() throws IOException{
        testState = new RemoveCrewState(0, ship1);
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
        testState.loseCrew(ship1, new Point(7,7));
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }
}