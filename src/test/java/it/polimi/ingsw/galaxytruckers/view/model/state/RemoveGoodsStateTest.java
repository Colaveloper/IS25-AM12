package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

class RemoveGoodsStateTest {
    @Test
    void testGetAvailableActions_whenCurrentShipEqualsMyShip() {
        ShipBoard ship = mock(ShipBoard.class);
        RemoveGoodsState state = new RemoveGoodsState(ship, 1, ship);
        List<StateActions> actions = state.getAvailableActions();
        assertTrue(actions.contains(StateActions.LOSE_GOOD));
    }

    @Test
    void testGetAvailableActions_whenCurrentShipNotEqualsMyShip() {
        ShipBoard myShip = mock(ShipBoard.class);
        ShipBoard otherShip = mock(ShipBoard.class);
        RemoveGoodsState state = new RemoveGoodsState(myShip, 1, otherShip);
        List<StateActions> actions = state.getAvailableActions();
        assertFalse(actions.contains(StateActions.LOSE_GOOD));
    }

    @Test
    void testNotifyRemoveGoods() {
        ShipBoard ship = mock(ShipBoard.class);
        Game game = mock(Game.class);
        when(game.getObservers()).thenReturn(Collections.singletonList(Mockito.mock(ModelObserver.class)));
        RemoveGoodsState state = new RemoveGoodsState(ship, 1, ship);
        Point point = new Point(1, 1);
        GoodsType goodsType = GoodsType.RED;
        state.setGame(game);
        state.notifyRemoveGoods(ship, point, goodsType);
        verify(ship).removeGoods(point, goodsType);
        verify(game.getObservers().get(0)).notifyRemoveGoods(ship, point, goodsType);
        assertEquals(0, state.getGoodsToLose());
    }

    @Test
    void testNotifyUseBattery() {
        ShipBoard ship = mock(ShipBoard.class);
        Game game = mock(Game.class);
        when(game.getObservers()).thenReturn(Collections.singletonList(Mockito.mock(ModelObserver.class)));
        RemoveGoodsState state = new RemoveGoodsState(ship, 1, ship);
        Point point = new Point(1, 1);
        state.setGame(game);
        state.notifyUseBattery(ship, point);
        verify(ship).useBattery(point);
        verify(game.getObservers().get(0)).notifyUseBattery(ship, point);
        assertEquals(0, state.getGoodsToLose());
    }
}