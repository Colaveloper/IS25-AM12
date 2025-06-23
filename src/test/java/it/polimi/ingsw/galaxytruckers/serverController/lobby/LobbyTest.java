package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.ShipBoardStub;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.GameSnapshotEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.LobbyEvent;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LobbyTest {
    Lobby lobby;
    Game game;
    Player p1;
    Consumer<Lobby> removeLobby;
    Runnable customRunnable;
    EventQueue<LobbyEvent> eventQueue;

    Point p = new Point(0, 0);
    CrewType crewType = CrewType.HUMAN;
    GoodsType goodsType = GoodsType.RED;

    @BeforeEach
    void setUp() {
        eventQueue = mock(EventQueue.class);
        game = mock(Game.class);
        p1 = Player.addPlayer("p1");
        when(game.addShipBoard(any(GameColor.class))).thenAnswer(i -> new ShipBoardStub(i.getArgument(0)));
        removeLobby = mock(Consumer.class);
        customRunnable = () -> {
        };
        doAnswer(_ -> {
            customRunnable.run();
            return null;
        }).when(removeLobby).accept(any(Lobby.class));
        lobby = new Lobby(game, p1, Level.SECOND, 2, removeLobby);
        lobby.stopEventHandler();
        lobby.setEventQueue(eventQueue);
    }

    @Test
    void testSetup() {
        assertEquals(Level.SECOND, lobby.getLevel());
        assertEquals(2, lobby.getNumPlayers());
        assertEquals(p1, lobby.getHost());
        assertEquals(List.of(p1), lobby.getPlayers());
        assertEquals(LobbyState.PREPARATION, lobby.getState());
    }

    @Test
    void removeLobby() {
        lobby.remove();
        verify(removeLobby).accept(lobby);
    }

    @Test
    void notifyDisconnectionSchedulesRemoval() throws InterruptedException {
        lobby.setRemovalDelay(10);
        CountDownLatch latch = new CountDownLatch(1);
        customRunnable = latch::countDown;
        lobby.notifyPlayerDisconnection(p1);
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS));
    }

    @Test
    void notifyPlayerDisconnectionDoesNothingIfAlreadyScheduled() throws InterruptedException {
        lobby.setRemovalDelay(10);
        CountDownLatch latch = new CountDownLatch(1);
        customRunnable = latch::countDown;
        lobby.notifyPlayerDisconnection(p1);
        lobby.notifyPlayerDisconnection(Player.addPlayer("p2"));
        lobby.notifyPlayerDisconnection(p1);
        lobby.notifyPlayerReconnection(p1);
        assertFalse(latch.await(100, TimeUnit.MILLISECONDS));
    }

    @Test
    void notifyPlayerDisconnectionWith2PlayersDoesNothing() throws InterruptedException {
        lobby.setRemovalDelay(10);
        CountDownLatch latch = new CountDownLatch(1);
        customRunnable = latch::countDown;
        lobby.addPlayer(Player.addPlayer("x"));
        lobby.notifyPlayerDisconnection(p1);
        assertFalse(latch.await(100, TimeUnit.MILLISECONDS));
    }

    @Test
    void addPlayerUpdatesPlayersAndStartsGame() {
        Player p2 = Player.addPlayer("p2");
        lobby.addPlayer(p2);
        assertEquals(LobbyState.INGAME, lobby.getState());
    }

    @Test
    void addPlayerCancelsRemoval() throws InterruptedException {
        lobby.setRemovalDelay(30);
        CountDownLatch latch = new CountDownLatch(1);
        customRunnable = latch::countDown;
        lobby.notifyPlayerDisconnection(p1);
        Player p2 = Player.addPlayer("p2");
        lobby.addPlayer(p2);
        assertFalse(latch.await(100, TimeUnit.MILLISECONDS));
    }

    @Test
    void notifyPlayerReconnectionCancelsRemoval() throws InterruptedException {
        lobby.setRemovalDelay(30);
        CountDownLatch latch = new CountDownLatch(1);
        customRunnable = latch::countDown;
        lobby.notifyPlayerDisconnection(p1);
        lobby.notifyPlayerReconnection(p1);
        assertFalse(latch.await(100, TimeUnit.MILLISECONDS));
    }

    @Test
    void notifyPlayerReconnectionCancelsRemovalAndRequestsSnapshot() {
        lobby.notifyPlayerDisconnection(p1);
        lobby.notifyPlayerReconnection(p1);
        GameSnapshotEvent event = new GameSnapshotEvent("p1", DtoConverter.getLobbyDetails(lobby), null);
        verify(eventQueue).notifyEvent(event);
    }

    @Test
    void notifyPlayerReconnectionRequestsSnapshot() throws InterruptedException {
        lobby.setRemovalDelay(30);
        CountDownLatch latch = new CountDownLatch(1);
        customRunnable = latch::countDown;
        lobby.addPlayer(Player.addPlayer("p2"));
        lobby.notifyPlayerDisconnection(p1);
        lobby.notifyPlayerReconnection(p1);
        assertFalse(latch.await(100, TimeUnit.MILLISECONDS));
        verify(game).requestSnapshot(p1.getShipBoard().orElseThrow());
    }

    @Nested
    class InGameTests {
        Player p2;

        @BeforeEach
        void setup() {
            p2 = Player.addPlayer("p2");
            lobby.addPlayer(p2);
        }

        @Test
        void skipWhenScheduledRemovalDoesNothing() {
            lobby.notifyPlayerDisconnection(p1);
            lobby.notifyPlayerDisconnection(p2);
            clearInvocations(game);
            lobby.skip(p1);
            verifyNoInteractions(game);
        }

        @Test
        void skip() {
            clearInvocations(game);
            lobby.skip(p1);
            verify(game).skip(p1.getShipBoard().orElseThrow());
        }

        @Test
        void requestRandComponent() {
            lobby.requestRandComponent(p1);
            verify(game).requestRandComponent(p1.getShipBoard().orElseThrow());
        }

        @Test
        void requestComponent() {
            lobby.requestComponent(p1, 0);
            verify(game).requestComponent(p1.getShipBoard().orElseThrow(), 0);
        }

        @Test
        void rejectComponent() {
            lobby.rejectComponent(p1);
            verify(game).rejectComponent(p1.getShipBoard().orElseThrow());
        }

        @Test
        void stashComponent() {
            lobby.stashComponent(p1);
            verify(game).stashComponent(p1.getShipBoard().orElseThrow());
        }

        @Test
        void grabPlacedComponent() {
            lobby.grabPlacedComponent(p1);
            verify(game).grabPlacedComponent(p1.getShipBoard().orElseThrow());
        }

        @Test
        void grabStashedComponent() {
            lobby.grabStashedComponent(p1, 0);
            verify(game).grabStashedComponent(p1.getShipBoard().orElseThrow(), 0);
        }

        @Test
        void placeComponent() {
            lobby.placeComponent(p1, p, Direction.UP);
            verify(game).placeComponent(p1.getShipBoard().orElseThrow(), p, Direction.UP);
        }

        @Test
        void flipHourglass() {
            lobby.flipHourglass(p1);
            verify(game).flipHourglass(p1.getShipBoard().orElseThrow());
        }

        @Test
        void placeShipOnFlightBoard() {
            lobby.placeShipOnFlightBoard(p1);
            verify(game).placeShipOnFlightBoard(p1.getShipBoard().orElseThrow());
        }

        @Test
        void placeShipOnFlightBoardWithPosition() {
            lobby.placeShipOnFlightBoard(p1, 0);
            verify(game).placeShipOnFlightBoard(p1.getShipBoard().orElseThrow(),0);
        }

        @Test
        void acquireForecast() {
            lobby.acquireForecast(p1, 0);
            verify(game).acquireForecast(p1.getShipBoard().orElseThrow(), 0);
        }

        @Test
        void releaseForecast() {
            lobby.releaseForecast(p1);
            verify(game).releaseForecast(p1.getShipBoard().orElseThrow());
        }

        @Test
        void removeComponent() {
            lobby.removeComponent(p1, p);
            verify(game).removeComponent(p1.getShipBoard().orElseThrow(), p);
        }

        @Test
        void chooseShipPiece() {
            lobby.chooseShipPiece(p1, 0);
            verify(game).chooseShipPiece(p1.getShipBoard().orElseThrow(), 0);
        }

        @Test
        void initializeCabin() {
            lobby.initializeCabin(p1, p, crewType);
            verify(game).initializeCabin(p1.getShipBoard().orElseThrow(), p, crewType);
        }

        @Test
        void drawCard() {
            lobby.drawCard(p1);
            verify(game).drawCard(p1.getShipBoard().orElseThrow());
        }

        @Test
        void activateComponent() {
            lobby.activateComponent(p1, p);
            verify(game).activateComponent(p1.getShipBoard().orElseThrow(), p);
        }

        @Test
        void loseCrew() {
            lobby.loseCrew(p1, p);
            verify(game).loseCrew(p1.getShipBoard().orElseThrow(), p);
        }

        @Test
        void grabReward() {
            lobby.grabReward(p1);
            verify(game).grabReward(p1.getShipBoard().orElseThrow());
        }

        @Test
        void placeGoods() {
            lobby.placeGoods(p1, p, goodsType);
            verify(game).placeGoods(p1.getShipBoard().orElseThrow(), p, goodsType);
        }

        @Test
        void removeGoods() {
            lobby.removeGoods(p1, p, goodsType);
            verify(game).removeGoods(p1.getShipBoard().orElseThrow(), p, goodsType);
        }

        @Test
        void loseGoods() {
            lobby.loseGoods(p1, p);
            verify(game).loseGood(p1.getShipBoard().orElseThrow(), p);
        }

        @Test
        void useBattery() {
            lobby.useBattery(p1, p);
            verify(game).useBattery(p1.getShipBoard().orElseThrow(), p);
        }

        @Test
        void choosePlanet() {
            lobby.choosePlanet(p1, 0);
            verify(game).choosePlanet(p1.getShipBoard().orElseThrow(), 0);
        }

        @Test
        void goNext() {
            lobby.goNext(p1);
            verify(game).goNext(p1.getShipBoard().orElseThrow());
        }

        @Test
        void giveUp() {
            lobby.giveUp(p1);
            verify(game).giveUp(p1.getShipBoard().orElseThrow());
        }


    }

    @Nested
    class WrongStateTests {
        @Test
        void skipDoesNothing() {
            lobby.skip(p1);
            verifyNoInteractions(game);
        }

        @Test
        void requestRandComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.requestRandComponent(p1));
        }

        @Test
        void requestComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.requestComponent(p1, 0));
        }

        @Test
        void rejectComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.rejectComponent(p1));
        }

        @Test
        void stashComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.stashComponent(p1));
        }

        @Test
        void grabPlacedComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.grabPlacedComponent(p1));
        }

        @Test
        void grabStashedComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.grabStashedComponent(p1, 0));
        }

        @Test
        void placeComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.placeComponent(p1, p, Direction.UP));
        }

        @Test
        void flipHourglass() {
            assertThrows(IllegalStateException.class, () -> lobby.flipHourglass(p1));
        }

        @Test
        void placeShipOnFlightBoard() {
            assertThrows(IllegalStateException.class, () -> lobby.placeShipOnFlightBoard(p1));
        }

        @Test
        void placeShipOnFlightBoardWithPosition() {
            assertThrows(IllegalStateException.class, () -> lobby.placeShipOnFlightBoard(p1, 0));
        }

        @Test
        void acquireForecast() {
            assertThrows(IllegalStateException.class, () -> lobby.acquireForecast(p1, 0));
        }

        @Test
        void releaseForecast() {
            assertThrows(IllegalStateException.class, () -> lobby.releaseForecast(p1));
        }

        @Test
        void removeComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.removeComponent(p1, p));
        }

        @Test
        void chooseShipPiece() {
            assertThrows(IllegalStateException.class, () -> lobby.chooseShipPiece(p1, 0));
        }

        @Test
        void initializeCabin() {
            assertThrows(IllegalStateException.class, () -> lobby.initializeCabin(p1, p, crewType));
        }

        @Test
        void drawCard() {
            assertThrows(IllegalStateException.class, () -> lobby.drawCard(p1));
        }

        @Test
        void activateComponent() {
            assertThrows(IllegalStateException.class, () -> lobby.activateComponent(p1, p));
        }

        @Test
        void loseCrew() {
            assertThrows(IllegalStateException.class, () -> lobby.loseCrew(p1, p));
        }

        @Test
        void grabReward() {
            assertThrows(IllegalStateException.class, () -> lobby.grabReward(p1));
        }

        @Test
        void placeGoods() {
            assertThrows(IllegalStateException.class, () -> lobby.placeGoods(p1, p, goodsType));
        }

        @Test
        void removeGoods() {
            assertThrows(IllegalStateException.class, () -> lobby.removeGoods(p1, p, goodsType));
        }

        @Test
        void loseGoods() {
            assertThrows(IllegalStateException.class, () -> lobby.loseGoods(p1, p));
        }

        @Test
        void useBattery() {
            assertThrows(IllegalStateException.class, () -> lobby.useBattery(p1, p));
        }

        @Test
        void choosePlanet() {
            assertThrows(IllegalStateException.class, () -> lobby.choosePlanet(p1,0));
        }

        @Test
        void goNext() {
            assertThrows(IllegalStateException.class, () -> lobby.goNext(p1));
        }

        @Test
        void giveUp() {
            assertThrows(IllegalStateException.class, () -> lobby.giveUp(p1));
        }
    }

    @AfterEach
    void cleanup() {
        Player.clear();
    }
}