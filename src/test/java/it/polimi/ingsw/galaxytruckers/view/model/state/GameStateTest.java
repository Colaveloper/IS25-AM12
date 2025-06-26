package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class GameStateTest {
    GameState state;

    @BeforeEach
    void setUp() {
        state = new GameState() {
            @Override
            public List<StateActions> getAvailableActions() {
                return List.of();
            }
        };
    }

    @Test
    void getGameAndSetGame() {
        Game game = mock(Game.class);
        state.setGame(game);
        assertEquals(game, state.getGame());
    }

    @Test
    void setMyShipSetsShip() {
        ShipBoard ship = mock(ShipBoard.class);
        state.setMyShip(ship);
        assertEquals(ship, state.myShip);
    }

    @Test
    void leaveCallsDeactivateAll() {
        Game game = mock(Game.class);
        ShipBoard ship1 = mock(ShipBoard.class);
        ShipBoard ship2 = mock(ShipBoard.class);
        when(game.getShipBoards()).thenReturn(Set.of(ship1, ship2));
        state.setGame(game);
        state.leave();
        verify(ship1).deactivateAll();
        verify(ship2).deactivateAll();
    }

    @Test
    void notifyRequestRandComponentDoesNotThrow() {
        state.notifyRequestRandComponent(mock(ShipBoard.class), mock(Component.class));
    }

    @Test
    void notifyRequestComponentDoesNotThrow() {
        state.notifyRequestComponent(mock(ShipBoard.class), mock(Component.class));
    }

    @Test
    void notifyRejectComponentDoesNotThrow() {
        state.notifyRejectComponent(mock(ShipBoard.class));
    }

    @Test
    void notifyStashComponentDoesNotThrow() {
        state.notifyStashComponent(mock(ShipBoard.class));
    }

    @Test
    void notifyGrabPlacedComponentDoesNotThrow() {
        state.notifyGrabPlacedComponent(mock(ShipBoard.class));
    }

    @Test
    void notifyGrabStashedComponentDoesNotThrow() {
        state.notifyGrabStashedComponent(mock(ShipBoard.class), 0);
    }

    @Test
    void notifyPlaceComponentDoesNotThrow() {
        state.notifyPlaceComponent(mock(ShipBoard.class), new Point(1, 2), Direction.UP);
    }

    @Test
    void notifyFlipHourglassDoesNotThrow() {
        state.notifyFlipHourglass(mock(ShipBoard.class));
    }

    @Test
    void notifyHourglassEndDoesNotThrow() {
        state.notifyHourglassEnd();
    }

    @Test
    void notifySurrenderShipRemovesShipsFromFlightBoard() {
        Game game = mock(Game.class);
        FlightBoard flightBoard = mock(FlightBoard.class);
        when(game.getFlightBoard()).thenReturn(flightBoard);
        ShipBoard ship = mock(ShipBoard.class);
        state.setGame(game);
        state.notifySurrenderShip(Set.of(ship));
        verify(flightBoard).removeShip(ship);
    }

    @Test
    void notifySurrenderRequestNotifiesObservers() {
        Game game = mock(Game.class);
        Player player = mock(Player.class);
        ModelObserver observer = mock(ModelObserver.class);
        when(game.getObservers()).thenReturn(List.of(observer));
        state.setGame(game);
        state.notifySurrenderRequest(player);
        verify(observer).notifyGiveUp(player);
    }

    @Test
    void notifyFlightBoardPositionSetsShipPosition() {
        Game game = mock(Game.class);
        FlightBoard flightBoard = mock(FlightBoard.class);
        when(game.getFlightBoard()).thenReturn(flightBoard);
        ShipBoard ship = mock(ShipBoard.class);
        state.setGame(game);
        state.notifyFlightBoardPosition(ship, 5, true);
        verify(flightBoard).setShipPosition(ship, 5);
    }

    @Test
    void notifyPeekForecastDoesNotThrow() {
        state.notifyPeekForecast(mock(ShipBoard.class), 0);
    }

    @Test
    void setForecastDeckDoesNotThrow() {
        AdventureCard dummyCard = mock(AdventureCard.class);
        state.setForecastDeck(List.of(dummyCard));
    }

    @Test
    void notifyReleaseForecastDoesNotThrow() {
        state.notifyReleaseForecast(mock(ShipBoard.class), true);
    }

    @Test
    void notifyRemoveComponentDoesNotThrow() {
        state.notifyRemoveComponent(mock(ShipBoard.class), new Point(1, 2));
    }

    @Test
    void notifyChooseShipPieceDoesNotThrow() {
        state.notifyChooseShipPiece(mock(ShipBoard.class), 1);
    }

    @Test
    void notifyShipNotConnectedDoesNotThrow() {
        state.notifyShipNotConnected(mock(ShipBoard.class), List.of(Set.of(new Point(1, 2))));
    }

    @Test
    void notifyShipValidatedDoesNotThrow() {
        state.notifyShipValidated(mock(ShipBoard.class));
    }

    @Test
    void notifyInitializeCabinDoesNotThrow() {
        state.notifyInitializeCabin(mock(ShipBoard.class), new Point(1, 2), CrewType.HUMAN);
    }

    @Test
    void notifyDrawCardDoesNotThrow() {
        state.notifyDrawCard(mock(AdventureCard.class));
    }

    @Test
    void notifyActivateComponentDoesNotThrow() {
        state.notifyActivateComponent(mock(ShipBoard.class), new Point(1, 2));
    }

    @Test
    void notifyLoseCrewDoesNotThrow() {
        state.notifyLoseCrew(mock(ShipBoard.class), new Point(1, 2));
    }

    @Test
    void notifyGrabRewardDoesNotThrow() {
        state.notifyGrabReward(mock(ShipBoard.class), 10);
    }

    @Test
    void notifyPlaceGoodsDoesNotThrow() {
        state.notifyPlaceGoods(mock(ShipBoard.class), new Point(1, 2), GoodsType.RED);
    }

    @Test
    void notifyRemoveGoodsDoesNotThrow() {
        state.notifyRemoveGoods(mock(ShipBoard.class), new Point(1, 2), GoodsType.RED);
    }

    @Test
    void notifyUseBatteryDoesNotThrow() {
        state.notifyUseBattery(mock(ShipBoard.class), new Point(1, 2));
    }

    @Test
    void notifyChoosePlanetDoesNotThrow() {
        state.notifyChoosePlanet(mock(ShipBoard.class), 1, mock(ShipBoard.class));
    }

    @Test
    void notifyCurrentPlayerUpdateDoesNotThrow() {
        state.notifyCurrentPlayerUpdate(mock(ShipBoard.class));
    }

    @Test
    void notifyGiveUpDoesNotThrow() {
        ShipBoard ship = mock(ShipBoard.class);
        when(ship.getColor()).thenReturn(GameColor.valueOf("RED"));
        state.notifyGiveUp(ship);
    }
}