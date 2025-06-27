package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
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