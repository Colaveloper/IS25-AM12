package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.checkerframework.dataflow.qual.AssertMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class HandleProjectileStateTest {
    HandleProjectileState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Projectile projectile;
    Dice dice;
    Game game;
    Deck deck;
    AdventureCard adventureCard;
    CountDownLatch latch;


    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.RED){
            @Override
            public int getNumBatteries() {
                return 2;
            }
            @Override
            public boolean activateComponent(Point pos){
                return true;
            }
            @Override
            public void useBatteries(Point pos){
                // mock
            }
            @Override
            public List<Set<Point>> getConnectedSets(){
                return List.of(Set.of(new Point(7,7)), Set.of(new Point(1,1)));
            }
        };
        dice = new Dice(){};
        projectile = new SmallMeteor(dice, Direction.LEFT){
            @Override
            public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
                return Set.of(new Point(7,7));
            }

            @Override
            public boolean fireAt(ShipBoard ship){
                return true;
            }
        };
        testState = new HandleProjectileState(ship1, projectile);
        game = new Game(Level.SECOND);
        game.setEventListener(new GameEventListenerStub());
        latch = StateTransitionUtils.setupLatch(game);
        testState.setGame(game);
    }

    @AssertMethod
    void assertNoTransition() {
        StateTransitionUtils.assertNoTransition(latch,game,testState);
    }

    @Test
    void getProjectile() {
        assertEquals(projectile, testState.getProjectile());
    }

    @Test
    void activateComponentThrowsExceptionWhenAlreadyActivatedAComponent(){
        testState.activateComponent(ship1, new Point(7,7));
        assertEquals(1, testState.batteriesToSpend);
        assertThrows(IllegalStateException.class, () -> testState.activateComponent(ship1, new Point(7,7)));
    }

    @Test
    void activateComponentActivatesComponent(){
        testState.activateComponent(ship1, new Point(7,7));
        assertEquals(1, testState.batteriesToSpend);
    }

    @Test
    void activateComponentChangesStateWhenHandlingIsDone() {
        testState.spendBatteries(ship1, new Point(7,7));
        testState.activateComponent(ship1, new Point(7,7));
        StateTransitionUtils.assertTransition(latch,game,ChooseShipPieceState.class);
    }

    @Test
    void spendBatteriesRemovesBattery() {
        testState.spendBatteries(ship1, new Point(7,7));
        assertEquals(-1, testState.batteriesToSpend);
    }

    @Test
    void spendBatteriesThrowsIfDoneMoreThanOnce() {
        testState.spendBatteries(ship1, new Point(7,7));
        assertThrows(IllegalStateException.class, () -> testState.spendBatteries(ship1, new Point(7,7)));
    }

    @Test
    void spendBatteriesChangesStateWhenHandlingIsDone() {
        testState.activateComponent(ship1, new Point(7,7));
        testState.spendBatteries(ship1, new Point(7,7));
        StateTransitionUtils.assertTransition(latch,game,ChooseShipPieceState.class);
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException. class, () -> testState.goNext(ship2));
    }

    @Test
    void goNextThrowsExceptionIfShipStillHasBatteriesToSpend(){
        testState.activateComponent(ship1, new Point(7,7));
        assertThrows(IllegalStateException.class, () -> testState.goNext(ship1));
    }

    @Test
    void goNextChangesAdventureStateToChooseShipPieceState(){
        game = new Game(Level.SECOND);
        game.setEventListener(new GameEventListenerStub());
        latch = StateTransitionUtils.setupLatch(game);
        testState.setGame(game);
        testState.goNext(ship1);
        StateTransitionUtils.assertTransition(latch,game,ChooseShipPieceState.class);
    }

    @Test
    void goNextChangesAdventureStateWithNextStep() throws IOException {
        projectile = new SmallMeteor(dice, Direction.LEFT){ // 1
            @Override
            public boolean fireAt(ShipBoard ship){
                return false;
            }
        };
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
        testState = new HandleProjectileState(ship1, projectile);
        testState.setGame(game);
        testState.goNext(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void goNextDoesNotSetAdventureStateToChooseShipPieceWithInsufficientShipPieces() throws IOException{
        ship1 = new SecondShipBoard(GameColor.RED){
            @Override
            public List<Set<Point>> getConnectedSets(){
                return List.of(Set.of(new Point(7,7)));
            }
        };
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
        testState = new HandleProjectileState(ship1, projectile);
        testState.setGame(game);
        testState.goNext(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);

    }

}