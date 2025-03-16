package it.polimi.ingsw.galaxytruckers.shipBuilding;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {

    ShipBoard shipBoard;

    @Nested
    @DisplayName("getConnectedSets() tests")
    class GetConnectedSetsTest {
        @BeforeEach
        void setUp() {
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
        }

        @Test
        void connectedSetsForConnectedShipsAre1() {
            assertEquals(1, shipBoard.getConnectedSets().size());
        }

        @Test
        void shipHas4differentConnectedSets() {
            shipBoard.removeComponent(new Point(7,7));

            assertEquals(4, shipBoard.getConnectedSets().size());
        }

        @Test
        void connectedSetsHaveTheRightMembers() {
            shipBoard.removeComponent(new Point(7,7));
            List<Set<Point>> expectedSets = new ArrayList<>();
            for (int i = 0; i < 4; i++) {expectedSets.add(new HashSet<>());}
            expectedSets.get(0).add(new Point(5,7));
            expectedSets.get(0).add(new Point(6,7));
            expectedSets.get(1).add(new Point(8,7));
            expectedSets.get(1).add(new Point(9,7));
            expectedSets.get(2).add(new Point(7,8));
            expectedSets.get(3).add(new Point(7,6));
            for (Set<Point> set : expectedSets) {
                assertTrue(shipBoard.getConnectedSets().contains(set));
            }
        }
    }




}