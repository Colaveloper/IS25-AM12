package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeclareFirePowerStateTest {

    @Test
    void testDeclareFirePowerState() {
        ShipBoard myShip = new SecondShipBoard(GameColor.BLUE);
        ShipBoard shipBoard = new SecondShipBoard(GameColor.RED);
        DeclareFirePowerState state = new DeclareFirePowerState(myShip, shipBoard);
        assertNotNull(state);
    }

}