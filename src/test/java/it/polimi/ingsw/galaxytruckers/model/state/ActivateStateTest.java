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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ActivateStateTest {
    ActivateState testActivateState;
    ShipBoard ship1;
    ShipBoard ship2;
    Game game;
    Deck deck;
    AdventureCard adventureCard;

    @BeforeEach
    void setup() {
        game = new Game(Level.SECOND);
        game.setEventListener(new  GameEventListenerStub());
        ship1 = new SecondShipBoard(GameColor.RED);
        ship2 = new SecondShipBoard(GameColor.BLUE){
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
        testActivateState = new ActivateStateStub(ship1) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.setGame(game);
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
        ship2 = new SecondShipBoard(GameColor.BLUE){
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
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        game.setEventListener(new GameEventListenerStub());
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