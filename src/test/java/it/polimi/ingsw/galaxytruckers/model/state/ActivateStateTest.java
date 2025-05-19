package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivateStateTest {
    ActivateState testActivateState;
    ShipBoard ship1;
    ShipBoard ship2;

    @BeforeEach
    void setup() {
        ship1 = new SecondShipBoard(FourColors.RED);
        ship2 = new SecondShipBoard(FourColors.BLUE){
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
        assertEquals(1, testActivateState.getBatteriesToSpend());
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
        assertEquals(0, testActivateState.getBatteriesToSpend());
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
    void goNextChangesGameState(){
        // TODO: finish test
    }
}