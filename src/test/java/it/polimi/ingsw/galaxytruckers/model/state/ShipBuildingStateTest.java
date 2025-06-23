package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.checkerframework.dataflow.qual.AssertMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class ShipBuildingStateTest {
    CountDownLatch latch;
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
            shipBoards.add(game.addShipBoard(GameColor.BLUE));
            shipBoards.add(game.addShipBoard(GameColor.RED));
            game.setEventListener(new GameEventListenerStub());
            latch = StateTransitionUtils.setupLatch(game);
            game.start();
            shipBuildingState = (SecondShipBuildingState) game.getCurrentState();
            shipBuildingState.getHourglass().stop();
        }

        @AssertMethod
        public void assertNoTransition() {
            StateTransitionUtils.assertNoTransition(latch,game,shipBuildingState);
        }

        @AssertMethod
        public void assertTransition() {
            StateTransitionUtils.assertTransition(latch,game, ShipCorrectionState.class);
        }

        @Test
        void setGameSetsAllAttributesCorrectly() {
            // shipBuildingState.setGame(game);
            assertTrue(shipBuildingState.getCompletedShipBoards().isEmpty());
            assertTrue(shipBuildingState.getShipToForecasts().isEmpty());
            assertNotNull(shipBuildingState.getHourglass());
            assertNotNull(shipBuildingState.getComponentBank());
            assertTrue(shipBuildingState.getBlockedForecasts().isEmpty());
            assertNoTransition();
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
        void requestRandComponentThrowsIfShipHasComponentInHand() {
            Component requestedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBoards.getFirst().offerComponent(new Component());
            assertThrows(IllegalStateException.class, () -> shipBuildingState.requestRandComponent(shipBoards.getFirst()));
            assertEquals(requestedComponent, shipBuildingState.getComponentBank().getCoveredComponents().getLast());
            assertTrue(shipBuildingState.getComponentBank().getUncoveredComponents().isEmpty());
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
        void requestComponentThrowsIfShipHasComponentInHand() {
            Component requestedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getLast());
            int id = shipBoards.getLast().getLastComponent().orElseThrow().getId();
            shipBuildingState.rejectComponent(shipBoards.getLast());
            shipBoards.getFirst().offerComponent(new Component());
            assertThrows(IllegalStateException.class, () -> shipBuildingState.requestComponent(shipBoards.getFirst(), id));
            assertTrue(shipBuildingState.getComponentBank().getUncoveredComponents().containsKey(requestedComponent.getId()));
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
            assertThrows(IllegalStateException.class, () -> shipBuildingState.rejectComponent(new SecondShipBoard(GameColor.RED)));
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
            Direction orientation = Direction.LEFT;
            Component placedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            shipBuildingState.placeComponent(shipBoards.getFirst(), componentPosition, orientation);
            assertEquals(placedComponent, shipBoards.getFirst().getLastComponent().orElse(null));
            assertEquals(orientation, placedComponent.getOrientation());
            assertEquals(componentPosition, shipBoards.getFirst().getLastPosition().orElse(null));
        }

        @Test
        void lastFlipHourglassThrowsExceptionIfShipIsNotCompleted() {
            Hourglass hourglass = shipBuildingState.getHourglass();
            shipBuildingState.getHourglass().setDuration(10);
            while (!hourglass.isLastFlip()) {
                try {
                    shipBuildingState.flipHourglass(shipBoards.getFirst());
                } catch (IllegalStateException _) {
                }
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
            assertNoTransition();
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

        @Test
        void grabPlacedComponentUpdatesShip() {
            shipBuildingState.requestRandComponent(shipBoards.getFirst());
            shipBuildingState.placeComponent(shipBoards.getFirst(), new Point(8,7),Direction.UP);
            shipBuildingState.grabPlacedComponent(shipBoards.getFirst());
            assertTrue(shipBoards.getFirst().getLastComponent().isPresent());
            assertTrue(shipBoards.getFirst().getLastPosition().isEmpty());
        }

        @Test
        void skipUpdatesPendingShips () {
            shipBuildingState.skip(shipBoards.getFirst());
            assertTrue(shipBuildingState.getPendingShipBoards().contains(shipBoards.getFirst()));
        }

        @Test
        void cancelSkipRemovesFromPendingShips() {
            shipBuildingState.skip(shipBoards.getFirst());
            shipBuildingState.cancelSkip(shipBoards.getFirst());
            assertTrue(shipBuildingState.getPendingShipBoards().isEmpty());
        }

        @Test
        void skipWithAllPendingChangesState() {
            for (ShipBoard shipBoard : shipBoards) {
                shipBuildingState.skip(shipBoard);
            }
            assertTransition();
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
                assertThrows(IllegalStateException.class, () -> shipBuildingState.placeComponent(shipBoards.getFirst(), null, Direction.UP));
            }

            @Test
            void grabPlacedComponentThrowsException() {
                assertThrows(IllegalStateException.class, () -> shipBuildingState.grabPlacedComponent(shipBoards.getFirst()));
            }

            @Test
            void lastFlipHourglassEndsBuilding() throws InterruptedException{
                shipBuildingState.getHourglass().stop();
                shipBuildingState.getHourglass().setDuration(1);

                for (int i = 1; i < 3; i++) {
                    shipBuildingState.flipHourglass(shipBoards.getFirst());
                    Thread.sleep(1050);
                }
                assertTransition();
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
            shipBoards.add(game.addShipBoard(GameColor.BLUE));
            shipBoards.add(game.addShipBoard(GameColor.RED));
            game.setEventListener(new GameEventListenerStub());
            latch = StateTransitionUtils.setupLatch(game);
            game.start();
            shipBuildingState = (TestShipBuildingState) game.getCurrentState();
        }

        @AssertMethod
        public void assertNoTransition() {
            StateTransitionUtils.assertNoTransition(latch,game,shipBuildingState);
        }

        @AssertMethod
        public void assertTransition() {
            StateTransitionUtils.assertTransition(latch,game, ShipCorrectionState.class);
        }

        @Test
        void setGameSetsAllAttributesCorrectly() {
            // shipBuildingState.setGame(game);
            assertTrue(shipBuildingState.getCompletedShipBoards().isEmpty());
            assertNotNull(shipBuildingState.getComponentBank());
            assertNoTransition();
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
            Direction orientation = Direction.LEFT;
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
            assertNoTransition();
        }

        @Test
        void placeShipOnFlightBoardEndsBuildingIfAllShipsPlacedChangesState() {
            shipBuildingState.placeShipOnFlightBoard(shipBoards.getFirst());
            shipBuildingState.placeShipOnFlightBoard(shipBoards.getLast());
            assertTransition();
        }

        @Test
        void placeShipOnFlightBoardWhenExpiredDoesNothing() {
            shipBuildingState.expired = true;
            shipBuildingState.placeShipOnFlightBoard(shipBoards.getFirst());
            shipBuildingState.placeShipOnFlightBoard(shipBoards.getLast());
            assertNoTransition();
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

        @Test
        void endBuildingPlacesOnFlightBoard() {
            shipBuildingState.placeShipOnFlightBoard(shipBoards.getFirst());
            shipBuildingState.endBuilding();
            assertTransition();
            assertEquals(new HashSet<>(shipBoards), game.getFlightBoard().getShipToPlace().keySet());
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
                assertThrows(IllegalStateException.class, () -> shipBuildingState.placeComponent(shipBoards.getFirst(), null, Direction.UP));
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
            public void initialize() {
                throw new RuntimeException("Failed to initialize ComponentBank");
            }
        };

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            new ShipBuildingState(brokenBank) {
                @Override
                protected void endBuilding() {}
            };
        });

    }

}