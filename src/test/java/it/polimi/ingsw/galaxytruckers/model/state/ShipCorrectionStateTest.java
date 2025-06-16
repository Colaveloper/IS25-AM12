package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.factory.SecondFactory;
import it.polimi.ingsw.galaxytruckers.model.factory.TestFactory;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ShipCorrectionStateTest {
    List<ShipBoard> shipBoards;
    ShipCorrectionState shipCorrectionState;
    Game game;

    @BeforeEach
    void setup() {
        shipBoards = new ArrayList<>();
    }

    @Nested
    class GeneralTests {

        @BeforeEach
        void setup() throws IOException {
            shipCorrectionState = new TestFactory().createShipCorrectionState();
            game = new Game(Level.TEST);
            shipBoards.add(game.addShipBoard(GameColor.BLUE));
            shipBoards.add(game.addShipBoard(GameColor.RED));
            shipBoards.add(game.addShipBoard(GameColor.GREEN));
            for (ShipBoard shipBoard : shipBoards) {
                shipBoard.removeComponent(new Point(7, 7));
            }
            game.setEventListener(new GameEventListenerStub());
            game.start();
            for (ShipBoard shipBoard : shipBoards) {
                shipBoard.offerComponent(new Cabin(
                        Map.of(
                                Direction.UP, Connector.UNIVERSAL,
                                Direction.LEFT, Connector.UNIVERSAL,
                                Direction.DOWN, Connector.UNIVERSAL,
                                Direction.RIGHT, Connector.UNIVERSAL
                        )));
                shipBoard.placeComponent(new Point(7, 7), Direction.UP);
                shipBoard.weldLastComponent();
            }
            shipBoards.get(1).offerComponent(new Component(
                    Map.of(
                            Direction.UP, Connector.NONE,
                            Direction.LEFT, Connector.NONE,
                            Direction.DOWN, Connector.NONE,
                            Direction.RIGHT, Connector.NONE
                    )
            ));
            shipBoards.get(1).placeComponent(new Point(8, 7), Direction.UP);
            shipBoards.get(1).weldLastComponent();
            shipBoards.get(2).offerComponent(new Component(
                    Map.of(
                            Direction.UP, Connector.NONE,
                            Direction.LEFT, Connector.NONE,
                            Direction.DOWN, Connector.NONE,
                            Direction.RIGHT, Connector.NONE
                    )
            ));
            shipBoards.get(2).placeComponent(new Point(8, 7), Direction.UP);
            shipBoards.get(2).weldLastComponent();
            shipBoards.get(2).offerComponent(new Component(
                    Map.of(
                            Direction.UP, Connector.NONE,
                            Direction.LEFT, Connector.NONE,
                            Direction.DOWN, Connector.NONE,
                            Direction.RIGHT, Connector.NONE
                    )
            ));
            shipBoards.get(2).placeComponent(new Point(9, 7), Direction.UP);
            shipBoards.get(2).weldLastComponent();
            game.getCurrentState().placeShipOnFlightBoard(shipBoards.getFirst());
            game.setCurrentState(shipCorrectionState);
        }

        @Test
        void removeComponentWithCorrectShipThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipCorrectionState.removeComponent(shipBoards.getFirst(), new Point(7, 7)));
        }

        @Test
        void removeComponentWithIncorrectShipUpdatesValidity() {
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
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
            assertTrue(shipCorrectionState.getShipPiecesMap().containsKey(shipBoards.get(2)));
        }

        @Test
        void shipRemainsInvalidAfterRemoval() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(9,7));
            assertFalse(shipCorrectionState.getValidShipBoards().contains(shipBoards.get(2)));
        }

        @Test
        void removeComponentWithAllValidShipsChangesState() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);
            game.setAfterEach(() -> {
                latch.countDown();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
            if (latch.await(1,TimeUnit.SECONDS)) {
                assertInstanceOf(ShipInitializationState.class, game.getCurrentState());
            }
            else throw new RuntimeException("Latch timed out");
        }

        @Test
        void chooseShipPieceUpdatesShipPieces() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            assertFalse(shipCorrectionState.getShipPiecesMap().containsKey(shipBoards.get(2)));
        }

        @Test
        void chooseShipPieceWithInvalidIndexThrowsException() {
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            assertThrows(IllegalArgumentException.class, () -> shipCorrectionState.chooseShipPiece(shipBoards.get(2), -1));
            assertThrows(IllegalArgumentException.class, () -> shipCorrectionState.chooseShipPiece(shipBoards.get(2), 20));
        }

        @Test
        void chooseShipPieceOnValidShipThrowsException() {
            assertThrows(IllegalStateException.class, () -> shipCorrectionState.chooseShipPiece(shipBoards.getFirst(), 0));
        }

        @Test
        void chooseShipPieceWithAllValidShipsChangesState() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(1);
            game.setAfterEach(() -> {
                latch.countDown();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(7, 7));
            shipCorrectionState.removeComponent(shipBoards.get(2), new Point(8, 7));
            shipCorrectionState.chooseShipPiece(shipBoards.get(2), 0);
            if (latch.await(1, TimeUnit.SECONDS)) assertInstanceOf(ShipInitializationState.class, game.getCurrentState());
            else throw new RuntimeException("Latch timed out");
        }
    }

    @Nested
    class SecondShipCorrectionTest {
        @BeforeEach
        void setup() throws IOException {
            shipCorrectionState = new SecondFactory().createShipCorrectionState();
            game = new Game(Level.SECOND);
            game.setEventListener(new GameEventListenerStub());
            shipBoards.add(game.addShipBoard(GameColor.BLUE));
            shipBoards.add(game.addShipBoard(GameColor.RED));
            game.start();
            game.placeShipOnFlightBoard(shipBoards.getFirst(), game.getFlightBoard().getStartingPositionsLeft().getFirst());
            shipBoards.get(1).offerComponent(new Component(
                    Map.of(
                            Direction.UP, Connector.NONE,
                            Direction.LEFT, Connector.NONE,
                            Direction.DOWN, Connector.NONE,
                            Direction.RIGHT, Connector.NONE
                    )
            ));
            shipBoards.get(1).placeComponent(new Point(8, 7), Direction.UP);
            shipBoards.get(1).weldLastComponent();
            shipBoards.get(1).offerComponent(new Component(
                    Map.of(
                            Direction.UP, Connector.NONE,
                            Direction.LEFT, Connector.NONE,
                            Direction.DOWN, Connector.NONE,
                            Direction.RIGHT, Connector.NONE
                    )
            ));
            shipBoards.get(1).placeComponent(new Point(6, 7), Direction.UP);
            shipBoards.get(1).weldLastComponent();
            game.setCurrentState(shipCorrectionState);
        }

        @Test
        void removeIncrementsLosses() {
            shipCorrectionState.removeComponent(shipBoards.get(1), new Point(8, 7));
            assertEquals(1, shipBoards.get(1).getLosses());
        }
    }
}