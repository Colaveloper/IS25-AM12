package it.polimi.ingsw.galaxytruckers;


import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.TestShipBoard;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {

    ShipBoard ship1, ship2, ship3;
    FlightBoard flightBoard;
    ComponentBank componentBank;

    @BeforeAll
    static void setup() {
        new JavaFXInitializer();
    }

    @Nested
    @DisplayName("Test-Flight Tests")
    class TestFlightTests {
        @BeforeEach
        void setUp() {
            ship1 = new TestShipBoard(componentBank, Colors.BLUE);
            ship2 = new TestShipBoard(componentBank, Colors.RED);
            ship3 = new TestShipBoard(componentBank, Colors.GREEN);
            flightBoard = new TestFlightBoard(Set.of(ship1, ship2, ship3));
        }

        @Test
        void placeShipOnFlightBoard() {
            assertEquals(Set.of(ship1, ship2, ship3), flightBoard.getAllShips());

            // starting position ignored in test flight
            assertFalse(flightBoard.placeShipOnFlightBoard(ship1, 123));
            assertFalse(flightBoard.placeShipOnFlightBoard(ship2, 324));
            assertTrue(flightBoard.placeShipOnFlightBoard(ship3, 123));

            Map<ShipBoard, Integer> shipToPlace = flightBoard.getShipToPlace();
            Deque<Integer> startingPositions = new ArrayDeque<>(Level.TEST.getStartingPositions());
            assertEquals(shipToPlace.get(ship1), startingPositions.pop());
            assertEquals(shipToPlace.get(ship2), startingPositions.pop());
            assertEquals(shipToPlace.get(ship3), startingPositions.pop());
            assertEquals(Set.of(ship1, ship2, ship3), flightBoard.getAllShips());
        }

        @Test
        void invalidCallsThrowExceptions() {
            assertThrows(UnsupportedOperationException.class, () -> flightBoard.removeShips(Set.of(ship1)));
            assertThrows(UnsupportedOperationException.class, () -> flightBoard.getAndRemoveLappedShips());
            assertThrows(UnsupportedOperationException.class, () -> flightBoard.giveUp(ship1));
        }
    }


    @Nested
    @DisplayName("Second-Flight Tests")
    class SecondFlightTests {
        @BeforeEach
        void setUp() {
            ship1 = new SecondShipBoard(componentBank, Colors.BLUE);
            ship2 = new SecondShipBoard(componentBank, Colors.RED);
            ship3 = new SecondShipBoard(componentBank, Colors.GREEN);
            flightBoard = new SecondFlightBoard(Set.of(ship1, ship2, ship3));
        }

        @Test
        void legalShipPlacementElseThrowExceptions() {
            assertEquals(Set.of(ship1, ship2, ship3), flightBoard.getAllShips());

            List<Integer> legalStartingPositions = Level.SECOND
                    .getStartingPositions()
                    .subList(0, flightBoard.getAllShips().size());

            // Asking for non-existing starting position
            assertThrows(
                    IllegalArgumentException.class,
                    () -> flightBoard.placeShipOnFlightBoard(ship1, 123)
            );

            // Adding ship 1
            assertFalse(flightBoard.placeShipOnFlightBoard(ship1, legalStartingPositions.get(1)));

            // Can't add two ships in the same position
            assertThrows(
                    IllegalArgumentException.class,
                    () -> flightBoard.placeShipOnFlightBoard(ship2, legalStartingPositions.get(1))
            );

            // Adding ship 2
            assertTrue(flightBoard.placeShipOnFlightBoard(ship2, legalStartingPositions.get(2)));

            // Adding ship 3: Building is over
            assertFalse(flightBoard.placeShipOnFlightBoard(ship3, legalStartingPositions.get(0)));
            assertEquals(flightBoard.getShipToPlace().get(ship1), legalStartingPositions.get(1));
            assertEquals(flightBoard.getShipToPlace().get(ship2), legalStartingPositions.get(2));
            assertEquals(flightBoard.getShipToPlace().get(ship3), legalStartingPositions.get(0));
            assertEquals(Arrays.asList(ship3, ship1, ship2), flightBoard.getOrderedShips());

            // 1's position incremented by 10+1, 1 becomes leader
            flightBoard.displaceShip(ship1, 10);
            assertEquals(Arrays.asList(ship1, ship3, ship2), flightBoard.getOrderedShips());
            assertEquals(legalStartingPositions.get(1) + 11, flightBoard.getShipToPlace().get(ship1));

            // 1 laps the others, they get removed
            flightBoard.displaceShip(ship1, 100);
            assertEquals(Set.of(ship2, ship3), flightBoard.getAndRemoveLappedShips()); // TODO: non pure getter?!

            // 1 is first and has the most component among the survivors
            assertEquals(Map.of(ship1, 6, ship2, 0, ship3, 0), flightBoard.getFinalScores());
            assertEquals(Set.of(ship1, ship2, ship3), flightBoard.getAllShips());
        }
    }
}