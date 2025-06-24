package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicCardTest {

    Game game;
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
        cabinAtRisk = new Cabin(Map.of(
                Direction.UP, Connector.UNIVERSAL,
                Direction.LEFT, Connector.UNIVERSAL,
                Direction.DOWN, Connector.UNIVERSAL,
                Direction.RIGHT, Connector.UNIVERSAL)) {
            @Override
            public int getNumResidents () {
                return 2;
            }
        };
        emptyCabin = new Cabin(Map.of(
                Direction.UP, Connector.UNIVERSAL,
                Direction.LEFT, Connector.UNIVERSAL,
                Direction.DOWN, Connector.UNIVERSAL,
                Direction.RIGHT, Connector.UNIVERSAL)) {
            @Override
            public int getNumResidents () {
                return 0;
            }
        };
        unconnectedCabin = new Cabin(Map.of(
                Direction.UP, Connector.NONE,
                Direction.LEFT, Connector.NONE,
                Direction.DOWN, Connector.NONE,
                Direction.RIGHT, Connector.NONE)) {
            @Override
            public int getNumResidents () {
                return 2;
            }
        };
        cabinAtRisk.initialize(CrewType.HUMAN);

        plaguedCabins = new HashSet<>();

        ShipBoard shipBoardA = new SecondShipBoardForTesting(GameColor.GREEN) {
            @Override
            public Map<Point, Cabin> getCabins() {
                return testCabins;
            }

            @Override
            public void loseCrew(Point position) {
                plaguedCabins.add(position);
            }
        };

        ShipBoard shipBoardB = new SecondShipBoardForTesting(GameColor.GREEN) {
            @Override
            public Map<Point, Cabin> getCabins() {
                return testCabins;
            }

            @Override
            public void loseCrew(Point position) {
                plaguedCabins.add(position);
            }
        };

        ships.add(shipBoardA);
        ships.add(shipBoardB);
        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }

        FlightBoard flightBoard = new FlightBoard(new GameEventListenerForTesting()) {
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
            public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }
        };

        GameEventListener listener = new GameEventListenerForTesting();
        game = new GameStub(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }

            @Override
            public GameEventListener getEventListener() {
                return listener;
            }
        };

        epidemicCard = new EpidemicCard(game, Level.SECOND, 1);
        epidemicCard.initialize();

    }

    @Test
    void nextStepReturnsDrawCardState() {
        testState = epidemicCard.getNextState();
        assertInstanceOf(DrawCardState.class, testState);}

    @Test
    void crewIsNotLostIfCabinsAreUnconnected() {
        testCabins.put(new Point(7, 7), unconnectedCabin);
        testCabins.put(new Point(8, 7), unconnectedCabin);
        testState = epidemicCard.getNextState();
        assertFalse(plaguedCabins.contains(new Point(7, 7)));
        assertFalse(plaguedCabins.contains(new Point(8, 7)));
    }

    @Test
    void crewIsNotLostIfOneCabinIsEmpty() {
        testCabins.put(new Point(7, 7), cabinAtRisk);
        testCabins.put(new Point(8, 7), emptyCabin);
        testState = epidemicCard.getNextState();
        assertFalse(plaguedCabins.contains(new Point(7, 7)));
        assertFalse(plaguedCabins.contains(new Point(8, 7)));
    }

    @Test
    void crewIsLostIfAllConditionsAreMet() {
        testCabins.put(new Point(7, 7), cabinAtRisk);
        testCabins.put(new Point(8, 7), cabinAtRisk);
        testState = epidemicCard.getNextState();
        assertTrue(plaguedCabins.contains(new Point(7, 7)));
        assertTrue(plaguedCabins.contains(new Point(8, 7)));
    }
}