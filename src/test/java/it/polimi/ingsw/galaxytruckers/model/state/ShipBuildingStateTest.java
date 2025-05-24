package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ShipBuildingStateTest {
    Game game;
    List<ShipBoard> shipBoards;

    @BeforeEach
    void setUp() {
        shipBoards = new ArrayList<>();
    }

    @Nested
    class SecondShipBuildingStateTest {
        SecondShipBuildingState shipBuildingState;

        @BeforeEach
        void setUp() {
            game = new Game(Level.SECOND);
            shipBoards.add(game.addShipBoard(FourColors.BLUE));
            shipBoards.add(game.addShipBoard(FourColors.RED));
            game.setEventListener(new GameEventListenerStub());

            // Make the first ship invalid so that it does not automatically change state
            ShipBoard invalidShip = shipBoards.getFirst();
            invalidShip.offerComponent(new Component(
                    List.of(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)
            ));
            invalidShip.placeComponent(new Point(5,9), 0);
            invalidShip.weldLastComponent();
            invalidShip.offerComponent(new Component(
                    List.of(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE)
            ));
            invalidShip.placeComponent(new Point(6,9), 0);
            invalidShip.weldLastComponent();

            try {
                game.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            shipBuildingState = (SecondShipBuildingState) game.getCurrentState();
            shipBuildingState.getHourglass().stop();
        }

        @Test
        void setGameSetsAllAttributesCorrectly() {
            // shipBuildingState.setGame(game);
            assertTrue(shipBuildingState.getCompletedShipBoards().isEmpty());
            assertTrue(shipBuildingState.getShipToForecasts().isEmpty());
            assertNotNull(shipBuildingState.getHourglass());
            assertNotNull(shipBuildingState.getComponentBank());
            assertTrue(shipBuildingState.getBlockedForecasts().isEmpty());
        }

        @Test
        void requestRandComponentRemovesFromCoveredAndAddsToShipBoard() {
            List<Component> coveredComponents = shipBuildingState.getComponentBank().getCoveredComponents();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            assertEquals(coveredComponents.getLast(), shipBoards.getFirst().getLastComponent().orElse(null));
            assertTrue(shipBuildingState.getComponentBank().getUncoveredComponents().isEmpty());
            assertEquals(coveredComponents.subList(0, coveredComponents.size() - 1), shipBuildingState.getComponentBank().getCoveredComponents());
        }

        @Test
        void requestComponentRemovesFromUncoveredAndAddsToShipBoard() {
            Component requestedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getLast());
            shipBuildingState.rejectComponent(shipBoards.getLast());
            shipBuildingState.requestComponent(shipBoards.getFirst(), requestedComponent.getId());
            assertTrue(shipBuildingState.getComponentBank().getUncoveredComponents().isEmpty());
            assertEquals(requestedComponent, shipBoards.getFirst().getLastComponent().orElse(null));
        }

        @Test
        void rejectComponentRemovesFromShipBoardAndAddsToCovered() {
            Component rejectedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            shipBuildingState.rejectComponent(shipBoards.getFirst());
            assertEquals(1, shipBuildingState.getComponentBank().getUncoveredComponents().size());
            assertTrue(shipBuildingState.getComponentBank().getUncoveredComponents().containsValue(rejectedComponent));
            assertTrue(shipBoards.getFirst().getLastComponent().isEmpty());
        }

        @Test
        void rejectComponentThrowsExceptionWhenNoComponentPresent() {
            assertThrows(IllegalStateException.class, () -> shipBuildingState.rejectComponent(new SecondShipBoard(FourColors.RED)));
        }

        @Test
        void stashComponentPutsComponentInStashingArea() {
            Component stashedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            shipBuildingState.stashComponent(shipBoards.getFirst());
            assertNull(shipBoards.getFirst().getLastComponent().orElse(null));
            assertTrue(shipBoards.getFirst().getStashedComponents().contains(stashedComponent));
            assertEquals(1, shipBoards.getFirst().getStashedComponents().size());
        }

        @Test
        void grabStashedComponentRemovesFromStashedComponentsAndSetsLastComponent() {
            Component stashedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            shipBuildingState.stashComponent(shipBoards.getFirst());
            shipBuildingState.grabStashedComponent(shipBoards.getFirst(), 0);
            assertEquals(stashedComponent, shipBoards.getFirst().getLastComponent().orElse(null));
            assertTrue(shipBoards.getFirst().getStashedComponents().isEmpty());
        }

        @Test
        void placeComponentSetsLastPositionAndComponentOrientation() {
            Point componentPosition = new Point(7, 8);
            int orientation = 1;
            Component placedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            shipBuildingState.placeComponent(shipBoards.getFirst(), componentPosition, orientation);
            assertEquals(placedComponent, shipBoards.getFirst().getLastComponent().orElse(null));
            assertEquals(orientation, placedComponent.getOrientation());
            assertEquals(componentPosition, shipBoards.getFirst().getLastPosition().orElse(null));
        }

        @Test
        void lastFlipHourglassThrowsExceptionIfShipIsNotCompleted() throws InterruptedException {
            //TODO: decide whether to let players flip the hourglass before building
            Hourglass hourglass = shipBuildingState.getHourglass();
            shipBuildingState.getHourglass().setDuration(10);
            for (int i = 1; i < 2; i++) {
                try {
                    shipBuildingState.flipHourglass(shipBoards.getFirst());
                } catch (IllegalStateException e) {
                    throw new RuntimeException("Timer is still running, i = " + i);
                }
                Thread.sleep(20);
            }
            assertThrows(IllegalStateException.class, () -> shipBuildingState.flipHourglass(shipBoards.getFirst()));
        }

        @Test
        void placeShipOnFlightBoardUpdatesCompletedShips() {
            shipBuildingState.placeShipOnFlightBoard(
                    shipBoards.getFirst(),
                    game.getFlightBoard().getStartingPositionsLeft().getFirst());
            assertTrue(shipBuildingState.getCompletedShipBoards().contains(shipBoards.getFirst()));
            assertEquals(1, shipBuildingState.getCompletedShipBoards().size());
        }

        @Test
        void placeShipOnFlightBoardReleasesForecasts() {
            shipBuildingState.acquireForecast(shipBoards.getFirst(), 0);
            shipBuildingState.placeShipOnFlightBoard(
                    shipBoards.getFirst(),
                    game.getFlightBoard().getStartingPositionsLeft().getFirst());
            assertTrue(shipBuildingState.getShipToForecasts().isEmpty());
            assertTrue(shipBuildingState.getBlockedForecasts().isEmpty());
        }

        @Test
        void acquireForecastBlocksForecastAndUpdatesMapping() {
            shipBuildingState.acquireForecast(shipBoards.getFirst(), 0);
            assertEquals(1, shipBuildingState.getBlockedForecasts().size());
            assertTrue(shipBuildingState.getBlockedForecasts().contains(0));
            assertEquals(1, shipBuildingState.getShipToForecasts().size());
            assertEquals(0, (int) shipBuildingState.getShipToForecasts().get(shipBoards.getFirst()));
        }

        @Test
        void acquireForecastWhenBlockedThrowsException() {
            shipBuildingState.acquireForecast(shipBoards.getFirst(), 0);
            assertThrows(IllegalArgumentException.class, () -> shipBuildingState.acquireForecast(shipBoards.getLast(), 0));
        }

        @Test
        void releaseForecastUnlocksTheForecast() {
            shipBuildingState.acquireForecast(shipBoards.getFirst(), 0);
            shipBuildingState.releaseForecast(shipBoards.getFirst());
            assertTrue(shipBuildingState.getShipToForecasts().isEmpty());
            assertTrue(shipBuildingState.getBlockedForecasts().isEmpty());
        }

        @Nested
        class CompletedShipsExceptionTest {
            @BeforeEach
            void setUp() {
                shipBuildingState.placeShipOnFlightBoard(
                        shipBoards.getFirst(),
                        game.getFlightBoard().getStartingPositionsLeft().getFirst());
            }

            @Test
            void requestRandComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.requestRandComponent(shipBoards.getFirst()));
            }

            @Test
            void requestComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.requestComponent(shipBoards.getFirst(), 0));
            }

            @Test
            void rejectComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.rejectComponent(shipBoards.getFirst()));
            }

            @Test
            void stashComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.stashComponent(shipBoards.getFirst()));
            }

            @Test
            void grabStashedComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.grabStashedComponent(shipBoards.getFirst(), 0));
            }

            @Test
            void placeComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.placeComponent(shipBoards.getFirst(), null, 0));
            }

            @Test
            void lastFlipHourglassEndsBuilding() throws InterruptedException{
                shipBuildingState.getHourglass().stop();
                shipBuildingState.getHourglass().setDuration(10);

                for (int i = 1; i < 3; i++) {
                    try {
                        shipBuildingState.flipHourglass(shipBoards.getFirst());
                    } catch (IllegalStateException e) {
                        throw new RuntimeException("Timer is still running, i = " + i);
                    }
                    Thread.sleep(20);
                }

                assertNotEquals(game.getCurrentState(), shipBuildingState);
            }

            @Test
            void placeShipOnFlightBoardThrowsException() {
                assertThrows(IllegalStateException.class, () ->
                        shipBuildingState.placeShipOnFlightBoard(
                                shipBoards.getFirst(),
                                game.getFlightBoard().getStartingPositionsLeft().getFirst()
                        ));
            }

            @Test
            void acquireForecastThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.acquireForecast(shipBoards.getFirst(), 0));
            }
        }

    }
    @Nested
    class TestShipBuildingStateTest {
        TestShipBuildingState shipBuildingState;

        @BeforeEach
        void setUp() {
            game = new Game(Level.TEST);
            shipBoards.add(game.addShipBoard(FourColors.BLUE));
            shipBoards.add(game.addShipBoard(FourColors.RED));
            game.setEventListener(new GameEventListenerStub());

            // Make the first ship invalid so that it does not automatically change state
            ShipBoard invalidShip = shipBoards.getFirst();
            invalidShip.offerComponent(new Component(
                    List.of(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)
            ));
            invalidShip.placeComponent(new Point(5,9), 0);
            invalidShip.weldLastComponent();
            invalidShip.offerComponent(new Component(
                    List.of(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE)
            ));
            invalidShip.placeComponent(new Point(6,9), 0);
            invalidShip.weldLastComponent();

            try {
                game.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            shipBuildingState = (TestShipBuildingState) game.getCurrentState();
        }

        @Test
        void setGameSetsAllAttributesCorrectly() {
            // shipBuildingState.setGame(game);
            assertTrue(shipBuildingState.getCompletedShipBoards().isEmpty());
            assertNotNull(shipBuildingState.getComponentBank());
        }

        @Test
        void requestRandComponentRemovesFromCoveredAndAddsToShipBoard() {
            List<Component> coveredComponents = shipBuildingState.getComponentBank().getCoveredComponents();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            assertEquals(coveredComponents.getLast(), shipBoards.getFirst().getLastComponent().orElse(null));
            assertTrue(shipBuildingState.getComponentBank().getUncoveredComponents().isEmpty());
            assertEquals(coveredComponents.subList(0, coveredComponents.size() - 1), shipBuildingState.getComponentBank().getCoveredComponents());
        }

        @Test
        void requestComponentRemovesFromUncoveredAndAddsToShipBoard() {
            Component requestedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getLast());
            shipBuildingState.rejectComponent(shipBoards.getLast());
            shipBuildingState.requestComponent(shipBoards.getFirst(), requestedComponent.getId());
            assertTrue(shipBuildingState.getComponentBank().getUncoveredComponents().isEmpty());
            assertEquals(requestedComponent, shipBoards.getFirst().getLastComponent().orElse(null));
        }

        @Test
        void rejectComponentRemovesFromShipBoardAndAddsToCovered() {
            Component rejectedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            shipBuildingState.rejectComponent(shipBoards.getFirst());
            assertEquals(1, shipBuildingState.getComponentBank().getUncoveredComponents().size());
            assertTrue(shipBuildingState.getComponentBank().getUncoveredComponents().containsValue(rejectedComponent));
            assertTrue(shipBoards.getFirst().getLastComponent().isEmpty());
        }

        @Test
        void stashingIsUnsupported() {
            assertThrows(UnsupportedOperationException.class, () -> shipBuildingState.stashComponent(shipBoards.getFirst()));
        }

        @Test
        void grabStashedComponentIsUnsupported() {
            assertThrows(UnsupportedOperationException.class, () -> shipBuildingState.grabStashedComponent(shipBoards.getFirst(), 0));
        }

        @Test
        void placeComponentSetsLastPositionAndComponentOrientation() {
            Point componentPosition = new Point(7, 8);
            int orientation = 1;
            Component placedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            shipBuildingState.placeComponent(shipBoards.getFirst(), componentPosition, orientation);
            assertEquals(placedComponent, shipBoards.getFirst().getLastComponent().orElse(null));
            assertEquals(orientation, placedComponent.getOrientation());
            assertEquals(componentPosition, shipBoards.getFirst().getLastPosition().orElse(null));
        }

        @Test
        void flipHourglassIsUnsupported() {
            assertThrows(UnsupportedOperationException.class, () -> shipBuildingState.flipHourglass(shipBoards.getFirst()));
        }

        @Test
        void placeShipOnFlightBoardUpdatesCompletedShips() {
            int firstAvailablePosition = game.getFlightBoard().getStartingPositionsLeft().getFirst();
            shipBuildingState.placeShipOnFlightBoard(shipBoards.getFirst());
            assertTrue(shipBuildingState.getCompletedShipBoards().contains(shipBoards.getFirst()));
            assertEquals(1, shipBuildingState.getCompletedShipBoards().size());
            assertEquals(firstAvailablePosition, game.getFlightBoard().getShipToPlace().get(shipBoards.getFirst()));
        }

        @Test
        void placeShipOnFlightBoardEndsBuildingIfAllShipsPlaced() {
            shipBuildingState.placeShipOnFlightBoard(shipBoards.getFirst());
            shipBuildingState.placeShipOnFlightBoard(shipBoards.getLast());
            assertInstanceOf(ShipCorrectionState.class, game.getCurrentState());
        }

        @Test
        void placeShipOnFlightBoardWithSpecifiedPositionIsUnsupported() {
            assertThrows(UnsupportedOperationException.class, () -> shipBuildingState.placeShipOnFlightBoard(shipBoards.getFirst(), 0));
        }

        @Test
        void acquireForecastIsUnsupported() {
            assertThrows(UnsupportedOperationException.class, () -> shipBuildingState.acquireForecast(shipBoards.getFirst(), 0));
        }

        @Test
        void releaseForecastIsUnsupported() {
            assertThrows(UnsupportedOperationException.class, () -> shipBuildingState.releaseForecast(shipBoards.getFirst()));
        }

        @Nested
        class CompletedShipsExceptionTest {
            @BeforeEach
            void setUp() {
                shipBuildingState.placeShipOnFlightBoard(shipBoards.getFirst());
            }

            @Test
            void requestRandComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.requestRandComponent(shipBoards.getFirst()));
            }

            @Test
            void requestComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.requestComponent(shipBoards.getFirst(), 0));
            }

            @Test
            void rejectComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.rejectComponent(shipBoards.getFirst()));
            }

            @Test
            void placeComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.placeComponent(shipBoards.getFirst(), null, 0));
            }

            @Test
            void placeShipOnFlightBoardThrowsException() {
                assertThrows(IllegalStateException.class, () ->
                        shipBuildingState.placeShipOnFlightBoard(shipBoards.getFirst()));
            }
        }
    }

    /* The IOException path in the default constructor is exercised indirectly via
    * a test-only constructor; we accept this 1-line coverage gap*/
    @Test
    void constructorCatchesIOExceptionFromInitialize() {
        ComponentBank brokenBank = new ComponentBank() {
            @Override
            public void initialize() throws IOException {
                throw new IOException("Simulated failure");
            }
        };

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            new ShipBuildingState(brokenBank) {
                @Override
                protected void endBuilding() {}
            };
        });

        assertEquals("java.io.IOException: Simulated failure", thrown.getMessage());
    }

}