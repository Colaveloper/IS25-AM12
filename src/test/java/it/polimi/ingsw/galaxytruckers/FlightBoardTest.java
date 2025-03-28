package it.polimi.ingsw.galaxytruckers;


import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.TestShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest extends JavaFXInitializer {

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
            ship1 = new TestShipBoard(componentBank, Colors.BLUE);
            ship2 = new TestShipBoard(componentBank, Colors.RED);
            ship3 = new TestShipBoard(componentBank, Colors.GREEN);
            allShips = new ArrayList<>(List.of(ship1, ship2, ship3));
            flightBoard = new TestFlightBoard(Set.of(ship1, ship2, ship3));
        }

        @Test
        void getImageAndDescriptionFakeTest () {
            flightBoard.getImage();
            flightBoard.getDescription();
        }

        @Test
        void getAllShipsReturnsAllShips() {
            assertEquals(Set.of(ship1, ship2, ship3), flightBoard.getAllShips());
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
            assertThrows(UnsupportedOperationException.class, () -> flightBoard.getAndRemoveLappedShips());
            assertThrows(UnsupportedOperationException.class, () -> flightBoard.giveUp(ship1));
        }

        @Test
        void AssignFinishOrderReward() {
            flightBoard.placeShipOnFlightBoard(ship2, 324);
            shipToScore.put(ship2, 4);
            flightBoard.placeShipOnFlightBoard(ship1, 123);
            shipToScore.put(ship1, 3);
            flightBoard.placeShipOnFlightBoard(ship3, 123);
            shipToScore.put(ship3, 2);
            flightBoard.assignFinishOrderReward();
            assertEquals(shipToScore, flightBoard.finalScores);
        }

        @Test
        void AssignBestLookingShipReward() {
            ship1 = new ShipBoard(null, null) {
                @Override
                protected boolean containsPoint(Point point) {
                    return false;
                }

                @Override
                public Image getImage() {
                    return null;
                }

                @Override
                public int getExposedConnectorsNumber() {
                    return 5;
                }
            };
            ship2 = new ShipBoard(null, null) {
                @Override
                protected boolean containsPoint(Point point) {
                    return false;
                }

                @Override
                public Image getImage() {
                    return null;
                }

                @Override
                public int getExposedConnectorsNumber() {
                    return 5;
                }
            };
            ship3 = new ShipBoard(null, null) {
                @Override
                protected boolean containsPoint(Point point) {
                    return false;
                }

                @Override
                public Image getImage() {
                    return null;
                }

                @Override
                public int getExposedConnectorsNumber() {
                    return 8;
                }
            };
            flightBoard = new TestFlightBoard(Set.of(ship1, ship2, ship3));
            flightBoard.placeShipOnFlightBoard(ship1, 123);
            flightBoard.placeShipOnFlightBoard(ship2, 324);
            flightBoard.placeShipOnFlightBoard(ship3, 123);
            shipToScore.put(ship1, 2);
            shipToScore.put(ship2, 2);
            shipToScore.put(ship3, 0);
            flightBoard.assignBestLookingShipReward();
            assertEquals(shipToScore, flightBoard.finalScores);
        }

        @Test
        void countCreditsAndLosses() {
            int myCredits = 8;
            int myLosses = 9;
            ship1 = new TestShipBoard(null, null) {
                @Override
                protected boolean containsPoint(Point point) {
                    return false;
                }

                @Override
                public Image getImage() {
                    return null;
                }

                @Override
                public int getLosses() {
                    return myLosses;
                }

                @Override
                public int getCredits() {
                    return myCredits;
                }

            };
            flightBoard = new TestFlightBoard(Set.of(ship1));
            flightBoard.placeShipOnFlightBoard(ship1, 123);
            shipToScore.put(ship1, myCredits-myLosses);
            flightBoard.countCreditsAndLosses();
            assertEquals(shipToScore, flightBoard.finalScores);
        }

        @Test
        void countGoodsValue() {
            int myGoodValue = 9;
            ship1 = new TestShipBoard(null, null) {
                @Override
                protected boolean containsPoint(Point point) {
                    return false;
                }

                @Override
                public Image getImage() {
                    return null;
                }

                @Override
                public int getGoodsValue() {
                    return myGoodValue;
                }
            };
            ship2 = new TestShipBoard(null, null) {
                @Override
                protected boolean containsPoint(Point point) {
                    return false;
                }

                @Override
                public Image getImage() {
                    return null;
                }

                @Override
                public int getGoodsValue() {
                    return myGoodValue;
                }
            };
            flightBoard = new TestFlightBoard(Set.of(ship1, ship2));
            flightBoard.placeShipOnFlightBoard(ship1, 123);
            shipToScore.put(ship1, myGoodValue);
            shipToScore.put(ship2, (myGoodValue+1)/2);
            flightBoard.countGoodsValue();
            assertEquals(shipToScore, flightBoard.finalScores);
        }

        @Test
        void getFinalScores() {
            int myGoodValue = 9;
            int myCreditsAndLosses = 6;
            int myBestLookingAward = 2;
            int myFinishOrderAward = 2;
            class LocalFlightBoard extends FlightBoard {
                public LocalFlightBoard(Set<ShipBoard> allShips) {
                    super(allShips);
                }

                @Override
                public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                    return false;
                }

                @Override
                protected int getLoopLength() {
                    return 0;
                }

                @Override
                protected void assignBestLookingShipReward() {
                    this.finalScores.merge(ship1, myBestLookingAward, Integer::sum);
                }

                @Override
                protected void assignFinishOrderReward() {
                    this.finalScores.merge(ship1, myFinishOrderAward, Integer::sum);
                }

                @Override
                protected void countCreditsAndLosses() {
                    this.finalScores.merge(ship1, myCreditsAndLosses, Integer::sum);
                }

                @Override
                protected void countGoodsValue() {
                    this.finalScores.merge(ship1, myGoodValue, Integer::sum);
                }

                @Override
                public Image getImage() {
                    return null;
                }
            }
            LocalFlightBoard localFlightBoard = new LocalFlightBoard(Set.of(ship1));
            shipToScore.put(ship1, myBestLookingAward+myFinishOrderAward+myCreditsAndLosses+myGoodValue);
            assertEquals(shipToScore, localFlightBoard.getFinalScores());
        }

        @Test
        void getLoopLenght() {
            assertEquals(TestFlightBoard.loopLength, flightBoard.getLoopLength());
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
            allShips = new ArrayList<>(List.of(ship1, ship2, ship3));
            flightBoard = new SecondFlightBoard(Set.of(ship1, ship2, ship3));
            legalStartingPositions = new ArrayList<>(SecondFlightBoard.startingPositions
                    .subList(0, allShips.size()));
        }

        @Test
        void getImageAndDescriptionFakeTest () {
            flightBoard.getImage();
            flightBoard.getDescription();
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
        void giveUpRemovesShip() {
            int i = 0;
            for (ShipBoard ship : allShips) {
                flightBoard.placeShipOnFlightBoard(ship, legalStartingPositions.get(i));
                shipToPlace.put(ship, legalStartingPositions.get(i));
                i++;
            }
            flightBoard.giveUp(ship1);
            shipToPlace.remove(ship1);
            assertEquals(shipToPlace, flightBoard.getShipToPlace());
        }

        @Test
        void giveUpWithOneLeftChangesGameRules() {
            int i = 0;
            for (ShipBoard ship : allShips) {
                flightBoard.placeShipOnFlightBoard(ship, legalStartingPositions.get(i));
                i++;
            }
            flightBoard.giveUp(ship1);
            flightBoard.giveUp(ship2);
            assertTrue(true); // TODO: implement and test one-left logic
        }

        @Test
        void getAndRemoveLappedShips() {
            int i = 0;
            for (ShipBoard ship : allShips) {
                flightBoard.placeShipOnFlightBoard(ship, legalStartingPositions.get(i));
                i++;
            }
            flightBoard.displaceShip(ship1, 100);
            assertEquals(Set.of(ship2, ship3), flightBoard.getAndRemoveLappedShips());
            assertEquals(Set.of(ship1), flightBoard.getShipToPlace().keySet());
        }
    }
}