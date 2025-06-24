package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class DeclareEnginePowerStateTest {

    @Test
    void activateComponentRunsBaseClassMethod(){
        ShipBoard ship1 = new SecondShipBoardForTesting(GameColor.RED){
            @Override
            public int getEnginePower(){
                return 1;
            }
            @Override
            public int getNumBatteries(){
                return 1;
            }
        };
        DeclareEnginePowerState testState = new DeclareEnginePowerState(ship1);
        testState.activateComponent(ship1, new Point(7,7));
        assertEquals(1, testState.enginePower);
    }

}