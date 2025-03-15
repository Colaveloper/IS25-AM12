package it.polimi.ingsw.galaxytruckers.shipBuilding;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {

    private ShipBoard shipBoard;

    @Test
    void connectedSetsForConnectedShipsAre1() {
        ComponentBank bank = new ComponentBank(){
            @Override
            public Component getRanComponent() {
                return new Component(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL,Connector.UNIVERSAL,Connector.UNIVERSAL));
            }
        };

        shipBoard = new ShipBoard(bank, Level.SECOND, Colors.BLUE);
        for (int i = 5; i <= 9; i++) {
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(new Point(i,7));
        }
        shipBoard.requestRandComponent();
        shipBoard.placeComponent(new Point(7,6));
        shipBoard.requestRandComponent();
        shipBoard.placeComponent(new Point(7,8));
        shipBoard.requestRandComponent();

        assertEquals(1, shipBoard.getConnectedSets().size());
    }

    @Test
    void shipHas4differentConnectedSets() {
        ComponentBank bank = new ComponentBank(){
            @Override
            public Component getRanComponent() {
                return new Component(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL,Connector.UNIVERSAL,Connector.UNIVERSAL));
            }
        };

        shipBoard = new ShipBoard(bank, Level.SECOND, Colors.BLUE);
        for (int i = 5; i <= 9; i++) {
            shipBoard.requestRandComponent();
            shipBoard.placeComponent(new Point(i,7));
        }
        shipBoard.requestRandComponent();
        shipBoard.placeComponent(new Point(7,6));
        shipBoard.requestRandComponent();
        shipBoard.placeComponent(new Point(7,8));
        shipBoard.requestRandComponent();
        shipBoard.removeComponent(new Point(7,7));

        assertEquals(4, shipBoard.getConnectedSets().size());
    }
}