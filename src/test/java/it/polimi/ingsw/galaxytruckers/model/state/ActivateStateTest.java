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
import java.util.HashSet;

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
            public void useBatteries(Point pos, int amount){
                // mock
            }
        };
        testActivateState = new ActivateState(ship1) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                super.activateComponent(shipBoard, position);
            }
        };
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
        testActivateState = new ActivateState(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions = new HashSet<>();
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.activateComponent(ship2, new Point(7,7));
        assertEquals(1, testActivateState.batteriesToSpend);
    }

    @Test
    void activateComponentWithNoAvailablePositionDoesNotIncrementBatteriesToSpend(){
        testActivateState = new ActivateState(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions = new HashSet<>();
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
            public void useBatteries(Point pos, int amount){
                // mock
            }
        };
        testActivateState = new ActivateState(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions = new HashSet<>();
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.activateComponent(ship2, new Point(7,7));
        assertEquals(0, testActivateState.batteriesToSpend);
    }

    @Test
    void spendBatteriesThrowsExceptionWhenOutOfTurn() {
        assertThrows(IllegalStateException.class, () -> testActivateState.spendBatteries(ship2, new Point(7,7), 1));
    }

    @Test
    void spendBatteriesThrowsExceptionWhenSpendingMoreBatteriesThanRequired(){
        assertThrows(IllegalArgumentException.class, () -> testActivateState.spendBatteries(ship1, new Point(7,7), 1));
    }

    @Test
    void spendBatteriesUsesBatteries(){
        testActivateState = new ActivateState(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions = new HashSet<>();
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.activateComponent(ship2, new Point(7,7));
        testActivateState.spendBatteries(ship2, new Point(7,7), 1);
        assertEquals(0, testActivateState.batteriesToSpend);
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn() {
        assertThrows(IllegalStateException.class, () -> testActivateState.goNext(ship2));
    }

    @Test
    void goNextThrowsExceptionIfShipStillHasBatteriesToSpend(){
        testActivateState = new ActivateState(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions = new HashSet<>();
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.activateComponent(ship2, new Point(7,7));
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
        testActivateState = new ActivateState(ship2) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions = new HashSet<>();
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        testActivateState.setGame(game);
        testActivateState.activateComponent(ship2, new Point(7,7));
        testActivateState.spendBatteries(ship2, new Point(7,7), 1);
        testActivateState.goNext(ship2);
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
    }
}