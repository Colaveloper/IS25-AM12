package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Battery;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.DoubleCannon;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class DeclareFirePowerStateTest {

    @Test
    void activateComponentCallsBaseClassMethodAndUpdatesCurrentFirePower(){
        ShipBoard ship1 = new SecondShipBoardForTesting(GameColor.RED);
        ship1.addWeldedComponent(new DoubleCannon(), new Point(6,7), Direction.UP);
        ship1.addWeldedComponent(new Battery(3), new Point(5,7), Direction.UP);
        DeclareFirePowerState testState = new DeclareFirePowerState(ship1);
        testState.activateComponent(ship1, new Point(6,7));
        assertEquals(4, ship1.getFirePower());
    }

}