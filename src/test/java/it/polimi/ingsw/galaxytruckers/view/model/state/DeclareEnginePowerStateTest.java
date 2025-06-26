package it.polimi.ingsw.galaxytruckers.view.model.state;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.TestShipBoard;


import org.junit.jupiter.api.Test;

class DeclareEnginePowerStateTest {
    @Test
    void constructorWithNoActivatableEnginesShouldSucceed() {
        TestShipBoard myShip = new TestShipBoard(GameColor.BLUE);
        TestShipBoard shipBoard = new TestShipBoard(GameColor.RED);
        DeclareEnginePowerState state = new DeclareEnginePowerState(myShip, shipBoard);
        assertNotNull(state);
    }
}