package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipCorrectionStateTest {
    List<ShipBoard> shipBoards;

    @BeforeEach
    void setup() {
        shipBoards = new ArrayList<>();
    }

    @Nested
    class TestShipCorrectionStateTest {
        TestShipCorrectionState shipCorrectionState;
        Game game;

        @BeforeEach
        void setup() throws IOException {
            shipCorrectionState = new TestShipCorrectionState();
            game = new Game(Level.TEST);
            shipBoards.add(game.addShipBoard(GameColor.BLUE));
            shipBoards.add(game.addShipBoard(GameColor.RED));
            shipBoards.add(game.addShipBoard(GameColor.GREEN));
            game.setEventListener(new GameEventListenerStub());
            game.start();
            for (ShipBoard shipBoard : shipBoards) {
                shipBoard.offerComponent(new Cabin(
                        List.of(Connector.UNIVERSAL,
                                Connector.UNIVERSAL,
                                Connector.UNIVERSAL,
                                Connector.UNIVERSAL)));
                shipBoard.placeComponent(new Point(7, 7), 0);
                shipBoard.weldLastComponent();
            }
            shipBoards.get(1).offerComponent(new Component(
                    List.of(Connector.NONE, Connector.NONE, Connector.NONE,  Connector.NONE)
            ));
            shipBoards.get(1).placeComponent(new Point(8,7),0);
            shipBoards.get(1).weldLastComponent();
            shipBoards.get(2).offerComponent(new Component(
                    List.of(Connector.NONE, Connector.NONE, Connector.NONE,  Connector.NONE)
            ));
            shipBoards.get(2).placeComponent(new Point(8,7),0);
            shipBoards.get(2).weldLastComponent();
            shipBoards.get(2).offerComponent(new Component(
                    List.of(Connector.NONE, Connector.NONE, Connector.NONE,  Connector.NONE)
            ));
            shipBoards.get(2).placeComponent(new Point(9,7),0);
            shipBoards.get(2).weldLastComponent();
            game.getCurrentState().placeShipOnFlightBoard(shipBoards.getFirst());
            game.setCurrentState(shipCorrectionState);
        }

        @Test
        void removeComponentWithCorrectShipThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipCorrectionState.removeComponent(shipBoards.getFirst(), new Point(7,7)));
        }

        @Test
        void removeComponentWithIncorrectShipUpdatesValidity() {
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7,7));
            assertEquals(1, shipBoards.get(1).getComponentMap().size());
            assertTrue(shipCorrectionState.getValidShipBoards().contains(shipBoards.get(1)));
        }

        @Test
        void removeComponentDoesNotUpdateLosses() {
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
            assertEquals(0, shipBoards.get(1).getLosses());
        }

        @Test
        void removeComponentUpdatesShipPiecesWhenShipIsSplit() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            assertTrue(shipCorrectionState.getShipPieces().containsKey(shipBoards.get(2)));
        }

        @Test
        void removeComponentWithAllValidShipsChangesState() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
            assertInstanceOf(DrawCardState.class, game.getCurrentState());
        }

        @Test
        void chooseShipPieceUpdatesShipPieces() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            assertFalse(shipCorrectionState.getShipPieces().containsKey(shipBoards.get(2)));
        }

        @Test
        void chooseShipPieceWithInvalidIndexThrowsException() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            assertThrows(IllegalArgumentException.class, () -> shipCorrectionState.chooseShipPiece(shipBoards.get(2), -1));
        }

        @Test
        void chooseShipPieceOnValidShipThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipCorrectionState.chooseShipPiece(shipBoards.getFirst(), 0));
        }

        @Test
        void chooseShipPieceWithAllValidShipsChangesState() {
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            assertInstanceOf(DrawCardState.class, game.getCurrentState());
        }
    }

    @Nested
    class SecondShipCorrectionStateTest {
        SecondShipCorrectionState shipCorrectionState;
        Game game;

        @BeforeEach
        void setup() throws IOException {
            shipCorrectionState = new SecondShipCorrectionState();
            game = new Game(Level.TEST);
            shipBoards.add(game.addShipBoard(GameColor.BLUE));
            shipBoards.add(game.addShipBoard(GameColor.RED));
            shipBoards.add(game.addShipBoard(GameColor.GREEN));
            game.setEventListener(new GameEventListenerStub());
            game.start();
            for (ShipBoard shipBoard : shipBoards) {
                shipBoard.offerComponent(new Component(
                        List.of(Connector.UNIVERSAL,
                                Connector.UNIVERSAL,
                                Connector.UNIVERSAL,
                                Connector.UNIVERSAL)));
                shipBoard.placeComponent(new Point(7, 7), 0);
                shipBoard.weldLastComponent();
            }
            shipBoards.get(1).offerComponent(new Component(
                    List.of(Connector.NONE, Connector.NONE, Connector.NONE,  Connector.NONE)
            ));
            shipBoards.get(1).placeComponent(new Point(8,7),0);
            shipBoards.get(1).weldLastComponent();
            shipBoards.get(2).offerComponent(new Component(
                    List.of(Connector.NONE, Connector.NONE, Connector.NONE,  Connector.NONE)
            ));
            shipBoards.get(2).placeComponent(new Point(8,7),0);
            shipBoards.get(2).weldLastComponent();
            shipBoards.get(2).offerComponent(new Component(
                    List.of(Connector.NONE, Connector.NONE, Connector.NONE,  Connector.NONE)
            ));
            shipBoards.get(2).placeComponent(new Point(9,7),0);
            shipBoards.get(2).weldLastComponent();
            game.getCurrentState().placeShipOnFlightBoard(shipBoards.getFirst());  // prevent exceptions from following states
            shipBoards.getFirst().offerComponent(new LifeSupport(
                    List.of(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL),
                    CrewType.BROWN
            ));
            shipBoards.getFirst().placeComponent(new Point(6,7),0);
            shipBoards.getFirst().offerComponent(new Cabin(
                    List.of(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)
            ));
            shipBoards.getFirst().placeComponent(new Point(5,7),0);
            game.setCurrentState(shipCorrectionState);
        }

        @Test
        void removeComponentWithCorrectShipThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipCorrectionState.removeComponent(shipBoards.getFirst(), new Point(7,7)));
        }

        @Test
        void removeComponentWithIncorrectShipUpdatesValidity() {
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7,7));
            assertEquals(1, shipBoards.get(1).getComponentMap().size());
            assertTrue(shipCorrectionState.getValidShipBoards().contains(shipBoards.get(1)));
        }

        @Test
        void removeComponentDoesUpdateLosses() {
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
            assertEquals(1, shipBoards.get(1).getLosses());
        }

        @Test
        void removeComponentUpdatesShipPiecesWhenShipIsSplit() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            assertTrue(shipCorrectionState.getShipPieces().containsKey(shipBoards.get(2)));
        }

        @Test
        void removeComponentWithAllValidShipsChangesState() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
            assertNotEquals(shipCorrectionState, game.getCurrentState());
        }

        @Test
        void chooseShipPieceUpdatesShipPieces() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            assertFalse(shipCorrectionState.getShipPieces().containsKey(shipBoards.get(2)));
        }

        @Test
        void chooseShipPieceWithInvalidIndexThrowsException() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            assertThrows(IllegalArgumentException.class, () -> shipCorrectionState.chooseShipPiece(shipBoards.get(2), -1));
            assertThrows(IllegalArgumentException.class, () -> shipCorrectionState.chooseShipPiece(shipBoards.get(2), 2));
        }

        @Test
        void chooseShipPieceOnValidShipThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipCorrectionState.chooseShipPiece(shipBoards.getFirst(), 0));
        }

        @Test
        void chooseShipPieceWithAllValidShipsChangesState() {
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            assertNotEquals(shipCorrectionState, game.getCurrentState());
        }
    }
}