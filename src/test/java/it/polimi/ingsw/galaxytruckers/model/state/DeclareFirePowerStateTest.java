package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class DeclareFirePowerStateTest {

    @Test
    void activateComponentCallsBaseClassMethodAndUpdatesCurrentFirePower(){
        ShipBoard ship1 = new SecondShipBoard(FourColors.RED){
            @Override
            public int getFirePower(){
                return 1;
            }
            @Override
            public int getNumBatteries(){
                return 1;
            }
        };
        DeclareFirePowerState testState = new DeclareFirePowerState(ship1);
        testState.activateComponent(ship1, new Point(7,7));
        assertEquals(1, testState.currentFirePower);
    }

}