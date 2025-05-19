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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivateStateTest {
    Game game;
    List<ShipBoard> shipBoards;

    @BeforeEach
    void setup(){
        game = new Game(Level.SECOND);
    }

    @Test
    void activateComponentWithOutOfTurnShipboardThrowsException() {
        ShipBoard ship1 = new SecondShipBoard(FourColors.RED);
        ShipBoard ship2 = new SecondShipBoard(FourColors.BLUE);
        ActivateState activateState = new ActivateState(ship1) {
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                super.activateComponent(shipBoard, position);
            }
        };
        assertThrows(IllegalStateException.class, () -> activateState.activateComponent(ship2, new Point(7,7)));
    }

    @Test
    void spendBatteries() {
    }

    @Test
    void goNext() {
    }
}