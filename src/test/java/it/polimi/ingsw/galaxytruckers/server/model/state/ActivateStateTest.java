package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.*;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Battery;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.DoubleCannon;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ActivateStateTest {
    ActivateState testActivateState;
    ShipBoard ship1;
    ShipBoard ship2;
    Game game;
    Deck deck;
    AdventureCard adventureCard;
    CountDownLatch latch;

    @BeforeEach
    void setup() throws IOException {
        game = new GameStub(Level.SECOND);
        ship1 = new SecondShipBoardForTesting(GameColor.RED);
        ship1.addWeldedComponent(new DoubleCannon(), new Point(8,7), Direction.UP);
        ship1.addWeldedComponent(new Battery(3), new Point(6,7),Direction.UP);
        ship2 = new SecondShipBoardForTesting(GameColor.BLUE){
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
        };
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
        testActivateState = new ActivateState(ship1, ship1.getActivatables().keySet()) {};
        game.setCurrentState(testActivateState);
    }

    @Test
    void skipDoesNothingWhenWhenExpired() {
        testActivateState.expired = true;
        testActivateState.skip(ship1);
        StateTransitionUtils.assertNoTransition(latch,game,testActivateState);
    }

    @Test
    void skipDoesNothingWhenOutOfTurn() {
        testActivateState.skip(ship2);
        StateTransitionUtils.assertNoTransition(latch,game,testActivateState);
    }

    @Test
    void skipChangesState() {
        testActivateState.skip(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void skipActivatesComponentsAndUpdatesState() {
        testActivateState.spendBatteries(ship1,new Point(6,7));
        testActivateState.skip(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
        assertTrue(ship1.getActivatables().get(new Point(8,7)).isActive());
    }

    @Test
    void skipSpendsBatteriesAndUpdatesState() {
        testActivateState.activateComponent(ship1,new Point(8,7));
        testActivateState.skip(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
        assertEquals(2, ship1.getBatteries().get(new Point(6,7)).getNumBatteries());
    }

    @Test
    void stateSetup() {
        Set<Point> availablePositions = Set.of(new Point(7,7));
        testActivateState = new ActivateState(ship1, availablePositions) {
        };
        assertEquals(ship1, testActivateState.getShipBoard());
        assertEquals(availablePositions, testActivateState.getAvailablePositions());
    }

    @Test
    void activateComponentThrowsExceptionWhenOutOfTurn() {
        assertThrows(IllegalStateException.class, () -> testActivateState.activateComponent(ship2, new Point(7,7)));
    }

    @Test
    void activateComponentWithInsufficientBatteriesThrowsException(){
        ship1.removeComponent(new Point(6,7
        ));
        assertThrows(IllegalStateException.class, () -> testActivateState.activateComponent(ship1, new Point(7,7)));
    }

    @Test
    void activateComponentIncreasesBatteriesToSpend(){
        testActivateState = new ActivateStateStub(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.setGame(game);
        testActivateState.activateComponent(ship2, new Point(7,7));
        assertEquals(1, testActivateState.batteriesToSpend);
    }

    @Test
    void activateComponentWithNoAvailablePositionDoesNotIncrementBatteriesToSpend(){
        testActivateState = new ActivateStateStub(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.activateComponent(ship2, new Point(7,7));
        assertEquals(0, testActivateState.batteriesToSpend);
    }

    @Test
    void activateComponentDoesNotIncrementBatteriesToSpendWhenShipBoardDoesNotActivateComponent(){
        ship2 = new SecondShipBoardForTesting(GameColor.BLUE){
            @Override
            public int getNumBatteries() {
                return 2;
            }
            @Override
            public boolean activateComponent(Point pos){
                return false;
            }
            @Override
            public void useBatteries(Point pos){
                // mock
            }
        };
        testActivateState = new ActivateStateStub(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.activateComponent(ship2, new Point(7,7));
        assertEquals(0, testActivateState.batteriesToSpend);
    }

    @Test
    void spendBatteriesThrowsExceptionWhenOutOfTurn() {
        assertThrows(IllegalStateException.class, () -> testActivateState.spendBatteries(ship2, new Point(7,7)));
    }

    @Test
    void spendBatteriesUsesBatteries(){
        testActivateState = new ActivateStateStub(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.setGame(game);
        testActivateState.activateComponent(ship2, new Point(7,7));
        testActivateState.spendBatteries(ship2, new Point(7,7));
        assertEquals(0, testActivateState.batteriesToSpend);
    }

    @Test
    void spendBatteriesThrowsWhenNotEnoughComponentsToActivate() {
        testActivateState = new ActivateStateStub(ship2);
        assertThrows(IllegalStateException.class, () -> testActivateState.spendBatteries(ship2, new Point(7,7)));
    }

    @Test
    void spendBatteriesDoesNotThrowsIfThereAreEnoughActivatables() {
        testActivateState = new ActivateState(ship2, Set.of(new Point(0, 0))) {
        };
        game.setCurrentState(testActivateState);
        assertDoesNotThrow(() -> testActivateState.spendBatteries(ship2, new Point(7,7)));
        assertEquals(-1,  testActivateState.batteriesToSpend);
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn() {
        assertThrows(IllegalStateException.class, () -> testActivateState.goNext(ship2));
    }

    @Test
    void goNextThrowsExceptionIfShipStillHasBatteriesToSpend(){
        testActivateState = new ActivateStateStub(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.setGame(game);
        testActivateState.activateComponent(ship2, new Point(7,7));
        assertThrows(IllegalStateException.class, () -> testActivateState.goNext(ship2));
    }

    @Test
    void goNextThrowsExceptionWhenShipHasComponentsToActivate() {
        testActivateState = new ActivateState(ship2, Set.of(new Point(0, 0))) {
        };
        game.setCurrentState(testActivateState);
        testActivateState.spendBatteries(ship2, new Point(7,7));
        assertThrows(IllegalStateException.class, () -> testActivateState.goNext(ship2));
    }

    @Test
    void goNextChangesGameState() throws IOException {
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
        testActivateState = new ActivateStateStub(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.setGame(game);
        testActivateState.activateComponent(ship2, new Point(7,7));
        testActivateState.spendBatteries(ship2, new Point(7,7));
        testActivateState.goNext(ship2);
        assertNotEquals(testActivateState, game.getCurrentState());
    }
}