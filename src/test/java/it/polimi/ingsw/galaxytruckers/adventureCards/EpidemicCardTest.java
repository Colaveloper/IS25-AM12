package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;
import org.ietf.jgss.GSSManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicCardTest {

    EpidemicCard epidemicCard;
    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;
    ShipBoard testShip;
    int testDisplacement;
    Map<Point, Cabin> testCabins;
    GameState testState;
    Boolean removesCrew;
    Cabin cabin;
    Cabin unconnectedCabin;

    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();

        testCabins = new HashMap<>();
        cabin = new Cabin(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
        unconnectedCabin = new Cabin(null, Arrays.asList(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE));
        cabin.initialize(CrewType.HUMAN);

        removesCrew = false;

        ShipBoard shipBoardA = new SecondShipBoard(Colors.GREEN) {
            @Override
            public Map<Point, Cabin> getCabins() {
                return testCabins;
            }

            @Override
            public void loseCrew(Point position, int amount) {
                removesCrew = true;
            }
        };

        ShipBoard shipBoardB = new SecondShipBoard(Colors.GREEN) {
            @Override
            public Map<Point, Cabin> getCabins() {
                return testCabins;
            }

            @Override
            public void loseCrew(Point position, int amount) {
                removesCrew = true;
            }
        };

        ships.add(shipBoardA);
        ships.add(shipBoardB);
        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }

        FlightBoard flightBoard = new FlightBoard(null) {
            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return shipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {
                testShip = shipBoard;
                testDisplacement = displacement;
            }

            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }

            @Override
            public Image getImage() {
                return null;
            }
        };

        epidemicCard = new EpidemicCard(null, Level.SECOND, flightBoard);

    }

    @Test
    void nextStepDoesntAffectThisShip() {
        testCabins.put(new Point(7, 7), unconnectedCabin);
        testCabins.put(new Point(8, 7), unconnectedCabin);
        testState = epidemicCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
        assertEquals(false, removesCrew);
    }

    @Test
    void nextStepAffectShip() {
        testCabins.put(new Point(7, 7), cabin);
        testCabins.put(new Point(8, 7), cabin);
        testState = epidemicCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
        assertEquals(true, removesCrew);
    }
}