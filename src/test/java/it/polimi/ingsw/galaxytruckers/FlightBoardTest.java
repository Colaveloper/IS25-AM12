package it.polimi.ingsw.galaxytruckers;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.TestShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.security.Provider;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {

    ShipBoard ship1, ship2, ship3;
    FlightBoard flightBoard;
    ComponentBank componentBank;

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
            // starting position ignored in test flight
            assertFalse(flightBoard.placeShipOnFlightBoard(ship1, 123));
            assertFalse(flightBoard.placeShipOnFlightBoard(ship2, 324));
            assertTrue(flightBoard.placeShipOnFlightBoard(ship3, 123));

            Map<ShipBoard, Integer> shipToPlace = flightBoard.getShipToPlace();
            Deque<Integer> startingPositions = new ArrayDeque<>(Level.TEST.getStartingPositions());
            assertEquals(shipToPlace.get(ship1), startingPositions.pop());
            assertEquals(shipToPlace.get(ship2), startingPositions.pop());
            assertEquals(shipToPlace.get(ship3), startingPositions.pop());
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
            List<Integer> legalStartingPositions = Level.SECOND.getStartingPositions();
            // Asking for non-existing starting position
            assertThrows(
                    IllegalArgumentException.class,
                    () -> flightBoard.placeShipOnFlightBoard(ship1, 123)
            );
            // Adding ship 1
            assertFalse(flightBoard.placeShipOnFlightBoard(ship1, legalStartingPositions.get(1)));
            // Asking to add another ship at the same position
            assertThrows(
                    IllegalArgumentException.class,
                    () -> flightBoard.placeShipOnFlightBoard(ship2, legalStartingPositions.get(1))
            );

            assertFalse(flightBoard.placeShipOnFlightBoard(ship2, legalStartingPositions.get(2)));
            assertTrue(flightBoard.placeShipOnFlightBoard(ship3, legalStartingPositions.get(3)));

            Map<ShipBoard, Integer> shipToPlace = flightBoard.getShipToPlace();
            Deque<Integer> startingPositions = new ArrayDeque<>(Level.TEST.getStartingPositions());
            assertEquals(shipToPlace.get(ship1), legalStartingPositions.get(1));
            assertEquals(shipToPlace.get(ship2), legalStartingPositions.get(2));
            assertEquals(shipToPlace.get(ship3), legalStartingPositions.get(3));
        }
    }



    @Test
    void getOrderedShips() {
    }

    @Test
    void getLappedShips() {
    }

    @Test
    void displaceShip() {
    }

    @Test
    void removeShip() {
    }

    @Test
    void getFinalScores() {
    }

    @Test
    void giveUp() {
    }
}