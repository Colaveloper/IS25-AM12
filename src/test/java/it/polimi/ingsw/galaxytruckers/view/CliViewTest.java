package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CheatCodes;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.model.*;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipInitializationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CliViewTest {
    private CliView cliView;
    private ClientController controller;
    private ClientModel model;
    private CliScreenFactory screenFactory;
    private CliScreen screen;

    @BeforeEach
    void setUp() throws Exception {
        controller = mock(ClientController.class);
        model = mock(ClientModel.class);
        screenFactory = mock(CliScreenFactory.class);
        screen = mock(CliScreen.class);
        cliView = new CliView(controller, model);
        // using reflection because screenFactory is private in View
        java.lang.reflect.Field field = CliView.class.getSuperclass().getDeclaredField("screenFactory");
        field.setAccessible(true);
        field.set(cliView, screenFactory);
        when(screenFactory.createScreen((MetaState) any(), any(), any())).thenReturn(screen);
        when(screenFactory.createScreen(any(GameState.class), any(), any())).thenReturn(screen);
    }

    @Test
    void notifyMetaState() {
        cliView.notifyMetaState(MetaState.REGISTER);
        verify(screenFactory).createScreen(eq(MetaState.REGISTER), eq(model), eq(controller));
        verify(screen).render();
    }

    @Test
    void notifyCurrentState() {
        ShipInitializationState state = mock(ShipInitializationState.class);
        cliView.notifyCurrentState(state);
        verify(screenFactory).createScreen(eq(state), eq(model), eq(controller));
        verify(screen).render();
    }

    @Test
    void notifyNewLobby() {
        Lobby lobby = mock(Lobby.class);
        cliView.currentScreen = screen;
        cliView.notifyNewLobby(lobby);
        verify(screen).notifyNewLobby(lobby);
        verify(screen).render();
    }

    @Test
    void notifyRemoveLobby() {
        UUID uuid = UUID.randomUUID();
        cliView.currentScreen = screen;
        cliView.notifyRemoveLobby(uuid);
        verify(screen).notifyRemoveLobby(uuid);
        verify(screen).render();
    }

    @Test
    void notifyRequestRandComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Component c = mock(Component.class);
        cliView.currentScreen = screen;
        cliView.notifyRequestRandComponent(sb, c);
        verify(screen).notifyRequestRandComponent(sb, c);
        verify(screen).render();
    }

    @Test
    void notifyRequestComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Component c = mock(Component.class);
        cliView.currentScreen = screen;
        cliView.notifyRequestComponent(sb, c);
        verify(screen).notifyRequestComponent(sb, c);
        verify(screen).render();
    }

    @Test
    void notifyRejectComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Component c = mock(Component.class);
        cliView.currentScreen = screen;
        cliView.notifyRejectComponent(sb, c);
        verify(screen).notifyRejectComponent(sb, c);
        verify(screen).render();
    }

    @Test
    void notifyStashComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Component c = mock(Component.class);
        cliView.currentScreen = screen;
        cliView.notifyStashComponent(sb, c);
        verify(screen).notifyStashComponent(sb, c);
        verify(screen).render();
    }

    @Test
    void notifyGrabStashedComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Component c = mock(Component.class);
        cliView.currentScreen = screen;
        cliView.notifyGrabStashedComponent(sb, 1, c);
        verify(screen).notifyGrabStashedComponent(sb, 1, c);
        verify(screen).render();
    }

    @Test
    void notifyGrabPlacedComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyGrabPlacedComponent(sb, p);
        verify(screen).notifyGrabPlacedComponent(sb, p);
        verify(screen).render();
    }

    @Test
    void notifyPlaceComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        Direction d = Direction.UP;
        cliView.currentScreen = screen;
        cliView.notifyPlaceComponent(sb, p, d);
        verify(screen).notifyPlaceComponent(sb, p, d);
        verify(screen).render();
    }

    @Test
    void notifyFlipHourglass() {
        ShipBoard sb = mock(ShipBoard.class);
        cliView.currentScreen = screen;
        cliView.notifyFlipHourglass(sb);
        verify(screen).notifyFlipHourglass(sb);
        verify(screen).render();
    }

    @Test
    void notifyHourglassEnd() {
        cliView.currentScreen = screen;
        cliView.notifyHourglassEnd();
        verify(screen).notifyHourglassEnd();
        verify(screen).render();
    }

    @Test
    void notifyFlightBoardPosition() {
        ShipBoard sb = mock(ShipBoard.class);
        cliView.currentScreen = screen;
        cliView.notifyFlightBoardPosition(sb, 5);
        verify(screen).notifyFlightBoardPosition(sb, 5);
        verify(screen).render();
    }

    @Test
    void notifyPeekForecast() {
        ShipBoard sb = mock(ShipBoard.class);
        cliView.currentScreen = screen;
        cliView.notifyPeekForecast(sb, 2);
        verify(screen).notifyPeekForecast(sb, 2);
        verify(screen).render();
    }

    @Test
    void setForecastDeck() {
        List<AdventureCard> cards = Collections.singletonList(null);
        cliView.currentScreen = screen;
        cliView.setForecastDeck(cards);
        verify(screen).setForecastDeck(cards);
        verify(screen).render();
    }

    @Test
    void notifyReleaseForecast() {
        ShipBoard sb = mock(ShipBoard.class);
        cliView.currentScreen = screen;
        cliView.notifyReleaseForecast(sb, 3);
        verify(screen).notifyReleaseForecast(sb, 3);
        verify(screen).render();
    }

    @Test
    void notifyRemoveComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyRemoveComponent(sb, p);
        verify(screen).notifyRemoveComponent(sb, p);
        verify(screen).render();
    }

    @Test
    void notifyChooseShipPiece() {
        ShipBoard sb = mock(ShipBoard.class);
        List<Point> removed = List.of(new Point(1, 2));
        cliView.currentScreen = screen;
        cliView.notifyChooseShipPiece(sb, 1, removed);
        verify(screen).notifyChooseShipPiece(sb, 1, removed);
        verify(screen).render();
    }

    @Test
    void notifyShipNotConnected() {
        ShipBoard sb = mock(ShipBoard.class);
        List<Set<Point>> shipPieces = List.of(Set.of(new Point(1, 2)));
        cliView.currentScreen = screen;
        cliView.notifyShipNotConnected(sb, shipPieces);
        verify(screen).notifyShipNotConnected(sb, shipPieces);
        verify(screen).render();
    }

    @Test
    void notifyShipValidated() {
        ShipBoard sb = mock(ShipBoard.class);
        cliView.currentScreen = screen;
        cliView.notifyShipValidated(sb);
        verify(screen).notifyShipValidated(sb);
        verify(screen).render();
    }

    @Test
    void notifyInitializeCabin() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyInitializeCabin(sb, p, CrewType.HUMAN, 2);
        verify(screen).notifyComponentChange(sb, p);
        verify(screen).render();
    }

    @Test
    void notifyDrawCard() {
        cliView.currentScreen = screen;
        cliView.notifyDrawCard(null);
        verify(screen).notifyDrawCard(null);
        verify(screen).render();
    }

    @Test
    void notifyActivateComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyActivateComponent(sb, p);
        verify(screen).notifyActivateComponent(sb, p);
        verify(screen).render();
    }

    @Test
    void notifyLoseCrew() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyLoseCrew(sb, p);
        verify(screen).notifyComponentChange(sb, p);
        verify(screen).render();
    }

    @Test
    void notifyGrabReward() {
        ShipBoard sb = mock(ShipBoard.class);
        cliView.currentScreen = screen;
        cliView.notifyGrabReward(sb, true);
        verify(screen).notifyGrabReward(sb, true);
        verify(screen).render();
    }

    @Test
    void notifyPlaceGoods() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyPlaceGoods(sb, p, GoodsType.RED);
        verify(screen).notifyComponentChange(sb, p);
        verify(screen).render();
    }

    @Test
    void notifyRemoveGoods() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyRemoveGoods(sb, p, GoodsType.RED);
        verify(screen).notifyComponentChange(sb, p);
        verify(screen).render();
    }

    @Test
    void notifyUseBattery() {
        ShipBoard sb = mock(ShipBoard.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyUseBattery(sb, p);
        verify(screen).notifyUseBattery(sb, p);
        verify(screen).render();
    }

    @Test
    void notifyChoosePlanet() {
        ShipBoard sb = mock(ShipBoard.class);
        ShipBoard next = mock(ShipBoard.class);
        cliView.currentScreen = screen;
        cliView.notifyChoosePlanet(sb, 1, next);
        verify(screen).notifyChoosePlanet(sb, 1, next);
        verify(screen).render();
    }

    @Test
    void notifyCurrentPlayerUpdate() {
        ShipBoard sb = mock(ShipBoard.class);
        cliView.currentScreen = screen;
        cliView.notifyCurrentPlayerUpdate(sb);
        verify(screen).notifyCurrentPlayerUpdate(sb);
        verify(screen).render();
    }

    @Test
    void notifyGiveUp() {
        Player player = mock(Player.class);
        cliView.currentScreen = screen;
        cliView.notifyGiveUp(player);
        verify(screen).notifyGiveUp(player);
        verify(screen).render();
    }

    @Test
    void setFinalScores() {
        Map<Player, Integer> scores = Map.of(mock(Player.class), 10);
        cliView.currentScreen = screen;
        cliView.setFinalScores(scores);
        verify(screen).setFinalScores(scores);
        verify(screen).render();
    }

    @Test
    void testNotifyRejectComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Component c = mock(Component.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyRejectComponent(sb, c, p);
        verify(screen).notifyRejectComponent(sb, c, p);
        verify(screen).render();
    }

    @Test
    void testNotifyStashComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Component c = mock(Component.class);
        Point p = new Point(1, 2);
        cliView.currentScreen = screen;
        cliView.notifyStashComponent(sb, c, p);
        verify(screen).notifyStashComponent(sb, c, p);
        verify(screen).render();
    }

    @Test
    void testNotifyPlaceComponent() {
        ShipBoard sb = mock(ShipBoard.class);
        Point newP = new Point(1, 2);
        Point oldP = new Point(2, 3);
        Direction d = Direction.UP;
        cliView.currentScreen = screen;
        cliView.notifyPlaceComponent(sb, newP, d, oldP);
        verify(screen).notifyPlaceComponent(sb, newP, d, oldP);
        verify(screen).render();
    }

    @Test
    void reportError() {
        assertDoesNotThrow(() -> cliView.reportError("error message"));
    }
}
