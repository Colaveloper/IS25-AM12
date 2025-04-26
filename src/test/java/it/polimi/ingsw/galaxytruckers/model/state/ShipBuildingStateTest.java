package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


//TODO:
// - test state transition
// - test requesting component via ids
class ShipBuildingStateTest {
    ShipBuildingState shipBuildingState;
    Game game;
    List<ShipBoard> shipBoards;

    @BeforeEach
    void setUp() {
        game = new Game(Level.SECOND);
        shipBoards = new ArrayList<>();
        shipBoards.add(game.addShipBoard(Colors.BLUE));
        shipBoards.add(game.addShipBoard(Colors.RED));
        game.start();
        shipBuildingState = (ShipBuildingState) game.getCurrentState();
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
        //TODO: define Component identifiers in the JSON file and the constructor
        // shipBuildingState.requestComponent(shipBoards.getFirst(), 0);
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
        Point componentPosition = new Point(7,8);
        int orientation = 1;
        Component placedComponent = shipBuildingState.getComponentBank().getCoveredComponents().getLast();
        shipBuildingState.requestRandComponent(shipBoards.getFirst());
        shipBuildingState.placeComponent(shipBoards.getFirst(), componentPosition, orientation);
        assertEquals(placedComponent, shipBoards.getFirst().getLastComponent().orElse(null));
        assertEquals(orientation, placedComponent.getOrientation());
        assertEquals(componentPosition, shipBoards.getFirst().getLastPosition().orElse(null));
    }

    @Test
    void flipHourglass() {
        //TODO: handle hourglass event notification
        //TODO: fix hourglass implementation
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
        assertEquals(1,shipBuildingState.getBlockedForecasts().size());
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
            assertThrows(IllegalStateException.class, () -> shipBuildingState.requestComponent(shipBoards.getFirst(),0));
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
            assertThrows(IllegalStateException.class, () -> shipBuildingState.grabStashedComponent(shipBoards.getFirst(),0));
        }

        @Test
        void placeComponentThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipBuildingState.placeComponent(shipBoards.getFirst(),null, 0));
        }

        @Test
        void flipHourglass() {
            //TODO: handle hourglass event notification
            //TODO: fix hourglass implementation
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