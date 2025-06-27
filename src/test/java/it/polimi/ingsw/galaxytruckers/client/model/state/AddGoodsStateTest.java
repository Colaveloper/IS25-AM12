package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.client.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.client.model.Game;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddGoodsStateTest {

    @Test
    void getAvailableActions() {
        Map<GoodsType, Integer> buffer = new HashMap<>();
        buffer.put(GoodsType.RED, 1);
        ShipBoard myShip = mock(ShipBoard.class);
        AddGoodsState state = new AddGoodsState(myShip, buffer, myShip);
        List<StateActions> actions = state.getAvailableActions();
        assertTrue(actions.contains(StateActions.ADD_GOOD));
        assertTrue(actions.contains(StateActions.REMOVE_GOOD));
        assertTrue(actions.contains(StateActions.GO_NEXT));

        // not my turn
        ShipBoard otherShip = mock(ShipBoard.class);
        AddGoodsState state2 = new AddGoodsState(myShip, buffer, otherShip);
        List<StateActions> actions2 = state2.getAvailableActions();
        assertFalse(actions2.contains(StateActions.ADD_GOOD));
    }

    @Test
    void notifyPlaceGoods() {
        Map<GoodsType, Integer> buffer = new HashMap<>();
        buffer.put(GoodsType.RED, 1);
        ShipBoard myShip = mock(ShipBoard.class);
        AddGoodsState state = new AddGoodsState(myShip, buffer, myShip);
        ModelObserver observer = mock(ModelObserver.class);
        // manually set the observers list to avoid NPE
        List<ModelObserver> observerList = new ArrayList<>();
        observerList.add(observer);
        state.game = mock(Game.class);
        when(state.game.getObservers()).thenReturn(observerList);
        Point p = new Point(1,1);
        state.notifyPlaceGoods(myShip, p, GoodsType.RED);
        assertFalse(state.getGoodsBuffer().getGoodsBuffer().containsKey(GoodsType.RED));
        verify(myShip).placeGoods(p, GoodsType.RED);
        verify(observer).notifyPlaceGoods(myShip, p, GoodsType.RED);
    }

    @Test
    void notifyRemoveGoods() {
        Map<GoodsType, Integer> buffer = new HashMap<>();
        ShipBoard myShip = mock(ShipBoard.class);
        AddGoodsState state = new AddGoodsState(myShip, buffer, myShip);
        ModelObserver observer = mock(ModelObserver.class);
        List<ModelObserver> observerList = new ArrayList<>();
        observerList.add(observer);
        state.game = mock(Game.class);
        when(state.game.getObservers()).thenReturn(observerList);
        Point p = new Point(2,2);
        state.notifyRemoveGoods(myShip, p, GoodsType.BLUE);
        assertEquals(1, state.getGoodsBuffer().getGoodsBuffer().get(GoodsType.BLUE));
        verify(myShip).removeGoods(p, GoodsType.BLUE);
        verify(observer).notifyRemoveGoods(myShip, p, GoodsType.BLUE);
    }

    @Test
    void getGoodsBuffer() {
        Map<GoodsType, Integer> buffer = new HashMap<>();
        buffer.put(GoodsType.GREEN, 2);
        ShipBoard myShip = mock(ShipBoard.class);
        AddGoodsState state = new AddGoodsState(myShip, buffer, myShip);
        assertEquals(2, state.getGoodsBuffer().getGoodsBuffer().get(GoodsType.GREEN));
    }

    @Test
    void getAvailableActions_goodsBufferEmpty() {
        Map<GoodsType, Integer> buffer = new HashMap<>(); // empty buffer
        ShipBoard myShip = mock(ShipBoard.class);
        AddGoodsState state = new AddGoodsState(myShip, buffer, myShip);
        List<StateActions> actions = state.getAvailableActions();
        assertFalse(actions.contains(StateActions.ADD_GOOD)); // no goods to add
        assertTrue(actions.contains(StateActions.REMOVE_GOOD));
        assertTrue(actions.contains(StateActions.GO_NEXT));

        // not my turn
        ShipBoard otherShip = mock(ShipBoard.class);
        AddGoodsState state2 = new AddGoodsState(myShip, buffer, otherShip);
        List<StateActions> actions2 = state2.getAvailableActions();
        assertFalse(actions2.contains(StateActions.ADD_GOOD));
    }
}