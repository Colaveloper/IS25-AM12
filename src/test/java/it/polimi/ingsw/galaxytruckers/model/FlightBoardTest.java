package it.polimi.ingsw.galaxytruckers.model;


import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.TestShipBoard;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {

    ShipBoard ship1, ship2, ship3;
    FlightBoard flightBoard;
    ComponentBank componentBank;
    Map<ShipBoard, Integer> shipToPlace;
    Map<ShipBoard, Integer> shipToScore;
    List<ShipBoard> allShips;
    List<Integer> legalStartingPositions;

    @BeforeEach
    void setUp() {
        shipToPlace = new HashMap<>();
        shipToScore = new HashMap<>();
    }

    @Nested
    @DisplayName("Test-Flight Tests")
    class TestFlightTests {
        @BeforeEach
        void setUp() {
            ship1 = new TestShipBoard(Colors.BLUE);
            ship2 = new TestShipBoard(Colors.RED);
            ship3 = new TestShipBoard(Colors.GREEN);
            allShips = new ArrayList<>(List.of(ship1, ship2, ship3));
            flightBoard = new TestFlightBoard(Set.of(ship1, ship2, ship3));
        }

        @Test
        void placeShipOnFlightBoardNeverFailsAndIgnoresStartingPosition() {
            // starting position ignored in test flight
            assertFalse(flightBoard.placeShipOnFlightBoard(ship1, 123));
            shipToPlace.put(ship1, TestFlightBoard.startingPositions.get(0));
            assertFalse(flightBoard.placeShipOnFlightBoard(ship2, 324));
            shipToPlace.put(ship2, TestFlightBoard.startingPositions.get(1));
            assertTrue(flightBoard.placeShipOnFlightBoard(ship3, 123));
            shipToPlace.put(ship3, TestFlightBoard.startingPositions.get(2));

            for (ShipBoard ship : allShips) {
                assertEquals(shipToPlace.get(ship), flightBoard.getShipToPlace().get(ship));
            }
        }

        @Test
        void invalidCallsThrowExceptions() {
            assertThrows(UnsupportedOperationException.class, () -> flightBoard.removeShips(Set.of(ship1)));
            assertThrows(UnsupportedOperationException.class, () -> flightBoard.getLappedShips());
        }

        @Test
        void getLoopLength() {
            assertEquals(TestFlightBoard.loopLength, flightBoard.getLoopLength());
        }
    }


    @Nested
    @DisplayName("Second-Flight Tests")
    class SecondFlightTests {
        @BeforeEach
        void setUp() {
            ship1 = new SecondShipBoard(Colors.BLUE);
            ship2 = new SecondShipBoard(Colors.RED);
            ship3 = new SecondShipBoard(Colors.GREEN);
            allShips = new ArrayList<>(List.of(ship1, ship2, ship3));
            flightBoard = new SecondFlightBoard(Set.of(ship1, ship2, ship3));
            legalStartingPositions = new ArrayList<>(SecondFlightBoard.startingPositions
                    .subList(0, allShips.size()));
        }

        @Test
        void placeShipOnFlightBoardThrowsExceptionIfNotStartingPosition() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> flightBoard.placeShipOnFlightBoard(ship1, 123)
            );
        }

        @Test
        void placeShipOnFlightBoardThrowsExceptionIfPositionAlreadyTaken() {
            flightBoard.placeShipOnFlightBoard(ship1, legalStartingPositions.get(1));
            assertThrows(
                    IllegalArgumentException.class,
                    () -> flightBoard.placeShipOnFlightBoard(ship2, legalStartingPositions.get(1))
            );
        }

        @Test
        void placeShipOnFlightBoardReturnsTrueOnlyAtTheEndOfPlacing() {
            // testing "random" placing
            assertFalse(flightBoard.placeShipOnFlightBoard(ship1, legalStartingPositions.get(1)));
            assertFalse(flightBoard.placeShipOnFlightBoard(ship2, legalStartingPositions.get(0)));
            assertTrue(flightBoard.placeShipOnFlightBoard(ship3, legalStartingPositions.get(2)));
        }

        @Test
        void getShipToPlaceAndGetOrderedShipsReturnShipsAsPlaced() {

            flightBoard.placeShipOnFlightBoard(ship1, legalStartingPositions.get(1));
            shipToPlace.put(ship1, legalStartingPositions.get(1));
            flightBoard.placeShipOnFlightBoard(ship2, legalStartingPositions.get(0));
            shipToPlace.put(ship2, legalStartingPositions.get(0));
            flightBoard.placeShipOnFlightBoard(ship3, legalStartingPositions.get(2));
            shipToPlace.put(ship3, legalStartingPositions.get(2));

            for (ShipBoard ship : allShips) {
                assertEquals(shipToPlace.get(ship), flightBoard.getShipToPlace().get(ship));
            }
            assertEquals(ship1, flightBoard.getOrderedShips().get(1));
            assertEquals(ship2, flightBoard.getOrderedShips().get(0));
            assertEquals(ship3, flightBoard.getOrderedShips().get(2));
        }

        @Test
        void displaceShipDisplacesForwardsAndBackwards() {
            int i = 0;
            for (ShipBoard ship : allShips) {
                flightBoard.placeShipOnFlightBoard(ship, legalStartingPositions.get(i));
                shipToPlace.put(ship, legalStartingPositions.get(i));
                i++;
            }
            flightBoard.displaceShip(ship1, 10);
            shipToPlace.put(ship1, shipToPlace.get(ship1) + 10);
            flightBoard.displaceShip(ship3, -10);
            shipToPlace.put(ship3, shipToPlace.get(ship3) - 10);

            for (ShipBoard ship : allShips) {
                assertEquals(shipToPlace.get(ship), flightBoard.getShipToPlace().get(ship));
            }
        }

        @Test
        void displaceShipJumpOverShips() {
            int i = 0;
            for (ShipBoard ship : allShips) {
                flightBoard.placeShipOnFlightBoard(ship, legalStartingPositions.get(i));
                shipToPlace.put(ship, legalStartingPositions.get(i));
                i++;
            }
            flightBoard.displaceShip(ship2, 10);
            shipToPlace.put(ship2, shipToPlace.get(ship2) + 10 + 1);
            for (ShipBoard ship : allShips) {
                assertEquals(shipToPlace.get(ship), flightBoard.getShipToPlace().get(ship));
            }

            flightBoard.displaceShip(ship2, -10);
            shipToPlace.put(ship2, shipToPlace.get(ship2) - 10 - 1);
            for (ShipBoard ship : allShips) {
                assertEquals(shipToPlace.get(ship), flightBoard.getShipToPlace().get(ship));
            }
        }

        @Test
        void getLappedShips() {
            int i = 0;
            for (ShipBoard ship : allShips) {
                flightBoard.placeShipOnFlightBoard(ship, legalStartingPositions.get(i));
                i++;
            }
            flightBoard.displaceShip(ship1, 100);
            assertEquals(Set.of(ship2, ship3), flightBoard.getLappedShips());
        }
    }
}