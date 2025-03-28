package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;
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
    Set<Point> plaguedCabins;
    Cabin cabinAtRisk;
    Cabin unconnectedCabin;
    Cabin emptyCabin;

    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();

        testCabins = new HashMap<>();
        cabinAtRisk = new Cabin(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)) {
            @Override
            public int getNumResidents () {
                return 2;
            }
        };
        emptyCabin = new Cabin(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)) {
            @Override
            public int getNumResidents () {
                return 0;
            }
        };
        unconnectedCabin = new Cabin(null, Arrays.asList(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE)) {
            @Override
            public int getNumResidents () {
                return 2;
            }
        };
        cabinAtRisk.initialize(CrewType.HUMAN);

        plaguedCabins = new HashSet<>();

        ShipBoard shipBoardA = new SecondShipBoard(Colors.GREEN) {
            @Override
            public Map<Point, Cabin> getCabins() {
                return testCabins;
            }

            @Override
            public void loseCrew(Point position, int amount) {
                plaguedCabins.add(position);
            }
        };

        ShipBoard shipBoardB = new SecondShipBoard(Colors.GREEN) {
            @Override
            public Map<Point, Cabin> getCabins() {
                return testCabins;
            }

            @Override
            public void loseCrew(Point position, int amount) {
                plaguedCabins.add(position);
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
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public Image getImage() {
                return null;
            }
        };

        epidemicCard = new EpidemicCard(null, Level.SECOND, flightBoard);

    }

    @Test
    void nextStepReturnsDrawCardState() {
        testState = epidemicCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);}

    @Test
    void crewIsNotLostIfCabinsAreUnconnected() {
        testCabins.put(new Point(7, 7), unconnectedCabin);
        testCabins.put(new Point(8, 7), unconnectedCabin);
        testState = epidemicCard.nextStep();
        assertFalse(plaguedCabins.contains(new Point(7, 7)));
        assertFalse(plaguedCabins.contains(new Point(8, 7)));
    }

    @Test
    void crewIsNotLostIfOneCabinIsEmpty() {
        testCabins.put(new Point(7, 7), cabinAtRisk);
        testCabins.put(new Point(8, 7), emptyCabin);
        testState = epidemicCard.nextStep();
        assertFalse(plaguedCabins.contains(new Point(7, 7)));
        assertFalse(plaguedCabins.contains(new Point(8, 7)));
    }

    @Test
    void crewIsLostIfAllConditionsAreMet() {
        testCabins.put(new Point(7, 7), cabinAtRisk);
        testCabins.put(new Point(8, 7), cabinAtRisk);
        testState = epidemicCard.nextStep();
        assertTrue(plaguedCabins.contains(new Point(7, 7)));
        assertTrue(plaguedCabins.contains(new Point(8, 7)));
    }
}