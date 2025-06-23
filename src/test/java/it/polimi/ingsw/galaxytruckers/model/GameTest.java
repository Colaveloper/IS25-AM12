package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.factory.SecondFactory;
import it.polimi.ingsw.galaxytruckers.model.factory.TestFactory;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GameTest {
    Game game;
    List<ShipBoard> shipBoards;
    GameEventListener gameEventListener;
    GameState gameState;

    @BeforeEach
    void setUp() {
        game = new Game(Level.SECOND);
        shipBoards = new ArrayList<>();
        shipBoards.add(game.addShipBoard(GameColor.BLUE));
        shipBoards.add(game.addShipBoard(GameColor.GREEN));
        for (ShipBoard s : shipBoards) {
            s.initializeCabin(new Point(7,7), CrewType.HUMAN);
            game.getFlightBoard().placeShipOnFlightBoard(s);
        }
        gameEventListener = GameEventListenerStub.getMock();
        game.setEventListener(gameEventListener);
        gameState = mock(GameState.class);
        game.setCurrentState(gameState);
        clearInvocations(gameState);
    }

    @Test
    void gameIsCreatedCorrectly() {
        assertInstanceOf(SecondFactory.class, game.getGameFactory());
        game = new Game(Level.TEST);
        assertInstanceOf(TestFactory.class, game.getGameFactory());
    }

    @Test
    void requestGameSnapshotDoesNothingIfGameOver() {
        game.setGameOver(true);
        game.requestSnapshot(shipBoards.getFirst());
        verifyNoInteractions(gameEventListener);
    }

    @Test
    void requestGameSnapshotGeneratesEvent() {
        game.requestSnapshot(shipBoards.getFirst());
        verify(gameEventListener).requestSnapshot(game, shipBoards.getFirst());
    }

    @Test
    void gameIsNotCreatedIfLevelIsNotValid() {
        assertThrows(IllegalArgumentException.class, () -> new Game(Level.FIRST));
    }

    @Test
    void gameStartsCorrectly() {
        game.start();
        assertInstanceOf(ShipBuildingState.class,  game.getCurrentState());
        assertNotNull(game.getFlightBoard());
        assertNotNull(game.getDeck());
    }

    @Test
    void shipBoardsAreAddedCorrectly() {
        assertEquals(2, game.getShipBoards().size());
        assertTrue(game.getShipBoards().containsAll(shipBoards));
    }

    @Test
    void tryEndGameDoesNotEndIfGameOver() {
        game.setGameOver(true);
        assertFalse(game.tryEndGame());
    }

    @Test
    void tryEndGameEndsIfDeckIsEmpty() throws IOException {
        game.setDeck(new Deck(game) {
            @Override
            public boolean isEmpty() {
                return true;
            }
        });
        assertTrue(game.tryEndGame());
    }

    @Test
    void tryEndGameEndsIfShipsSurrendered() {
        for (ShipBoard s : game.getShipBoards()) {
            game.getSurrenderPolicy().requestSurrender(s, SurrenderCause.REQUEST);
        }
        game.getSurrenderPolicy().confirmSurrender(game.getFlightBoard());
        assertTrue(game.tryEndGame());
    }

    @Test
    void assignShipRewardsForFlightBoardAndBestLookingShip() throws IOException {
        game.setDeck(new Deck(game) {
            @Override
            public boolean isEmpty() {
                return true;
            }
        });
        assertTrue(game.tryEndGame());
        ScoresRegistry registry = game.getScoresRegistry();
        assertEquals(registry.getPositionScores()[0] + registry.getBestLookingShipAward(), game.getFinalScores().get(shipBoards.getFirst()));
        assertEquals(registry.getPositionScores()[1] + registry.getBestLookingShipAward(), game.getFinalScores().get(shipBoards.get(1)));
    }

    @Test
    void assignShipRewardsWithCreditsAndLosses() throws IOException {
        game.setDeck(new Deck(game) {
            @Override
            public boolean isEmpty() {
                return true;
            }
        });
        shipBoards.getFirst().discardComponent(shipBoards.getFirst().getCenter());
        shipBoards.getFirst().gainCredits(10);
        assertTrue(game.tryEndGame());
        ScoresRegistry registry = game.getScoresRegistry();
        assertEquals(
                registry.getPositionScores()[0] + registry.getBestLookingShipAward() + 10 - 1,
                game.getFinalScores().get(shipBoards.getFirst()));
        assertEquals(registry.getPositionScores()[1], game.getFinalScores().get(shipBoards.get(1)));
    }

    @Test
    void assignShipRewardsWithSurrenderedShips() throws IOException {
        game.setDeck(new Deck(game) {
            @Override
            public boolean isEmpty() {
                return true;
            }
        });
        ShipBoard ship1 = shipBoards.getFirst();
        Point p = new Point(8,7);
        ship1.addWeldedComponent(new CargoHold(3), p, Direction.UP);
        ship1.placeGoods(p, GoodsType.YELLOW, 1);
        for (ShipBoard shipBoard : shipBoards) {
            game.getSurrenderPolicy().requestSurrender(shipBoard, SurrenderCause.REQUEST);
        }
        game.getSurrenderPolicy().confirmSurrender(game.getFlightBoard());
        assertTrue(game.tryEndGame());
        assertEquals(2,  game.getFinalScores().get(shipBoards.getFirst()));
        assertEquals(0,   game.getFinalScores().get(shipBoards.get(1)));
    }

    @Test
    void skipDoesNothingWhenGameOver() {
        game.setGameOver(true);
        game.skip(shipBoards.getFirst());
        verifyNoInteractions(gameState);
    }

    @Test
    void skipWhenInGame() {
        game.skip(shipBoards.getFirst());
        verify(gameState).skip(shipBoards.getFirst());
    }

    @Test
    void requestRandComponent() {
        game.requestRandComponent(shipBoards.getFirst());
        verify(gameState).requestRandComponent(shipBoards.getFirst());
    }

    @Test
    void requestComponent() {
        game.requestComponent(shipBoards.getFirst(), 0);
        verify(gameState).requestComponent(shipBoards.getFirst(), 0);
    }

    @Test
    void rejectComponent() {
        game.rejectComponent(shipBoards.getFirst());
        verify(gameState).rejectComponent(shipBoards.getFirst());
    }

    @Test
    void stashComponent() {
        game.stashComponent(shipBoards.getFirst());
        verify(gameState).stashComponent(shipBoards.getFirst());
    }

    @Test
    void grabPlacedComponent() {
        game.grabPlacedComponent(shipBoards.getFirst());
        verify(gameState).grabPlacedComponent(shipBoards.getFirst());
    }

    @Test
    void grabStashedComponent() {
        game.grabStashedComponent(shipBoards.getFirst(), 0);
        verify(gameState).grabStashedComponent(shipBoards.getFirst(), 0);
    }

    @Test
    void placeComponent() {
        Point p = new Point(8,7);
        game.placeComponent(shipBoards.getFirst(),p,Direction.UP);
        verify(gameState).placeComponent(shipBoards.getFirst(),p,Direction.UP);
    }

    @Test
    void flipHourglass() {
        game.flipHourglass(shipBoards.getFirst());
        verify(gameState).flipHourglass(shipBoards.getFirst());
    }

    @Test
    void placeShipOnFlightBoard() {
        game.placeShipOnFlightBoard(shipBoards.getFirst());
        verify(gameState).placeShipOnFlightBoard(shipBoards.getFirst());
    }

    @Test
    void placeShipOnFlightBoardWithPosition() {
        game.placeShipOnFlightBoard(shipBoards.getFirst(),0);
        verify(gameState).placeShipOnFlightBoard(shipBoards.getFirst(),0);
    }

    @Test
    void acquireForecast() {
        game.acquireForecast(shipBoards.getFirst(),0);
        verify(gameState).acquireForecast(shipBoards.getFirst(),0);
    }

    @Test
    void releaseForecast() {
        game.releaseForecast(shipBoards.getFirst());
        verify(gameState).releaseForecast(shipBoards.getFirst());
    }

    @Test
    void removeComponent() {
        Point point = new Point(8,7);
        game.removeComponent(shipBoards.getFirst(),point);
        verify(gameState).removeComponent(shipBoards.getFirst(),point);
    }

    @Test
    void chooseShipPiece() {
        game.chooseShipPiece(shipBoards.getFirst(),0);
        verify(gameState).chooseShipPiece(shipBoards.getFirst(),0);
    }

    @Test
    void initializeCabin() {
        Point point = new Point(8,7);
        game.initializeCabin(shipBoards.getFirst(),point,CrewType.HUMAN);
        verify(gameState).initializeCabin(shipBoards.getFirst(),point,CrewType.HUMAN);
    }

    @Test
    void activateComponent() {
        Point point = new Point(8,7);
        game.activateComponent(shipBoards.getFirst(),point);
        verify(gameState).activateComponent(shipBoards.getFirst(),point);
    }

    @Test
    void loseCrew() {
        Point point = new Point(8,7);
        game.loseCrew(shipBoards.getFirst(),point);
        verify(gameState).loseCrew(shipBoards.getFirst(),point);
    }

    @Test
    void grabReward() {
        game.grabReward(shipBoards.getFirst());
        verify(gameState).grabReward(shipBoards.getFirst());
    }

    @Test
    void placeGoods() {
        Point point = new Point(8,7);
        game.placeGoods(shipBoards.getFirst(),point,GoodsType.RED);
        verify(gameState).addGood(shipBoards.getFirst(),point,GoodsType.RED);
    }

    @Test
    void removeGoods() {
        Point point = new Point(8,7);
        game.removeGoods(shipBoards.getFirst(),point,GoodsType.RED);
        verify(gameState).removeGood(shipBoards.getFirst(),point,GoodsType.RED);
    }

    @Test
    void useBattery() {
        Point point = new Point(8,7);
        game.useBattery(shipBoards.getFirst(),point);
        verify(gameState).spendBatteries(shipBoards.getFirst(),point);
    }

    @Test
    void choosePlanet() {
        game.choosePlanet(shipBoards.getFirst(),0);
        verify(gameState).choosePlanet(shipBoards.getFirst(),0);
    }

    @Test
    void giveUp() {
        game.giveUp(shipBoards.getFirst());
        verify(gameState).giveUp(shipBoards.getFirst());
    }

    @Test
    void drawCard() {
        game.drawCard(shipBoards.getFirst());
        verify(gameState).drawCard(shipBoards.getFirst());
    }

    @Test
    void loseGood() {
        Point point = new Point(8,7);
        game.loseGood(shipBoards.getFirst(),point);
        verify(gameState).loseGood(shipBoards.getFirst(),point);
    }

    @Test
    void goNext() {
        game.goNext(shipBoards.getFirst());
        verify(gameState).goNext(shipBoards.getFirst());
    }

    @Nested
    class FailedRequestTests {
        @BeforeEach
        void setup() {
            game.setGameOver(true);
        }

        @Test
        void requestRandComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.requestRandComponent(shipBoards.getFirst());
            });
        }

        @Test
        void requestComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.requestComponent(shipBoards.getFirst(),0);
            });
        }

        @Test
        void rejectComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.rejectComponent(shipBoards.getFirst());
            });
        }

        @Test
        void stashComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.stashComponent(shipBoards.getFirst());
            });
        }

        @Test
        void grabPlacedComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.grabPlacedComponent(shipBoards.getFirst());
            });
        }

        @Test
        void grabStashedComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.grabStashedComponent(shipBoards.getFirst(),0);
            });
        }

        @Test
        void placeComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.placeComponent(shipBoards.getFirst(),null,null);
            });
        }

        @Test
        void flipHourglass() {
            assertThrows(IllegalStateException.class, () -> {
                game.flipHourglass(shipBoards.getFirst());
            });
        }

        @Test
        void placeShipOnFlightBoard() {
            assertThrows(IllegalStateException.class, () -> {
                game.placeShipOnFlightBoard(shipBoards.getFirst());
            });
        }

        @Test
        void placeShipOnFlightBoardWithPosition() {
            assertThrows(IllegalStateException.class, () -> {
                game.placeShipOnFlightBoard(shipBoards.getFirst(),0);
            });
        }

        @Test
        void acquireForecast() {
            assertThrows(IllegalStateException.class, () -> {
                game.acquireForecast(shipBoards.getFirst(),0);
            });
        }

        @Test
        void releaseForecast() {
            assertThrows(IllegalStateException.class, () -> {
                game.releaseForecast(shipBoards.getFirst());
            });
        }

        @Test
        void removeComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.removeComponent(shipBoards.getFirst(),null);
            });
        }

        @Test
        void chooseShipPiece() {
            assertThrows(IllegalStateException.class, () -> {
                game.chooseShipPiece(shipBoards.getFirst(),0);
            });
        }

        @Test
        void initializeCabin() {
            assertThrows(IllegalStateException.class, () -> {
                game.initializeCabin(shipBoards.getFirst(), null,null);
            });
        }

        @Test
        void activateComponent() {
            assertThrows(IllegalStateException.class, () -> {
                game.activateComponent(shipBoards.getFirst(),null);
            });
        }

        @Test
        void loseCrew() {
            assertThrows(IllegalStateException.class, () -> {
                game.loseCrew(shipBoards.getFirst(),null);
            });
        }

        @Test
        void grabReward() {
            assertThrows(IllegalStateException.class, () -> {
                game.grabReward(shipBoards.getFirst());
            });
        }

        @Test
        void placeGoods() {
            assertThrows(IllegalStateException.class, () -> {
                game.placeGoods(shipBoards.getFirst(), null,null);
            });
        }

        @Test
        void removeGoods() {
            assertThrows(IllegalStateException.class, () -> {
                game.removeGoods(shipBoards.getFirst(), null,null);
            });
        }

        @Test
        void useBattery() {
            assertThrows(IllegalStateException.class, () -> {
                game.useBattery(shipBoards.getFirst(),null);
            });
        }

        @Test
        void choosePlanet() {
            assertThrows(IllegalStateException.class, () -> {
                game.choosePlanet(shipBoards.getFirst(), 0);
            });
        }

        @Test
        void giveUp() {
            assertThrows(IllegalStateException.class, () -> {
                game.giveUp(shipBoards.getFirst());
            });
        }

        @Test
        void drawCard() {
            assertThrows(IllegalStateException.class, () -> {
                game.drawCard(shipBoards.getFirst());
            });
        }

        @Test
        void loseGood() {
            assertThrows(IllegalStateException.class, () -> {
                game.loseGood(shipBoards.getFirst(),null);
            });
        }

        @Test
        void goNext() {
            assertThrows(IllegalStateException.class, () -> {
                game.goNext(shipBoards.getFirst());
            });
        }
    }
}