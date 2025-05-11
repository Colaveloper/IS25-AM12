package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;
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
        cabinAtRisk = new Cabin(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)) {
            @Override
            public int getNumResidents () {
                return 2;
            }
        };
        emptyCabin = new Cabin(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)) {
            @Override
            public int getNumResidents () {
                return 0;
            }
        };
        unconnectedCabin = new Cabin(Arrays.asList(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE)) {
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
        };

        game = new Game(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }
        };

        epidemicCard = new EpidemicCard(game, Level.SECOND);
        epidemicCard.initialize();

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