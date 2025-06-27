package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChoosePlanetStateTest {
    static class TestShipBoard extends ShipBoard {
        public TestShipBoard(GameColor color) { super(color); }
        @Override public Set<Point> getShipArea() { return new HashSet<>(); }
    }

    @Test
    void constructorAndIsMyTurn() {
        TestShipBoard myShip = new TestShipBoard(GameColor.BLUE);
        ChoosePlanetState state = new ChoosePlanetState(myShip, myShip, 2);
        assertTrue(state.isMyTurn());
        assertEquals(2, state.getOptions().length);
    }

    @Test
    void getAvailableActionsMyTurn() {
        TestShipBoard myShip = new TestShipBoard(GameColor.BLUE);
        ChoosePlanetState state = new ChoosePlanetState(myShip, myShip, 1);
        List<?> actions = state.getAvailableActions();
        assertTrue(actions.contains(StateActions.CHOOSE_PLANET));
        assertTrue(actions.contains(StateActions.GO_NEXT));
    }

    @Test
    void getAvailableActionsNotMyTurn() {
        TestShipBoard myShip = new TestShipBoard(GameColor.BLUE);
        TestShipBoard otherShip = new TestShipBoard(GameColor.RED);
        ChoosePlanetState state = new ChoosePlanetState(myShip, otherShip, 1);
        List<?> actions = state.getAvailableActions();
        assertFalse(actions.contains(StateActions.CHOOSE_PLANET));
    }

    @Test
    void notifyChoosePlanetSetsOptionAndTurn() {
        TestShipBoard myShip = new TestShipBoard(GameColor.BLUE);
        TestShipBoard nextShip = new TestShipBoard(GameColor.RED);
        ChoosePlanetState state = new ChoosePlanetState(myShip, myShip, 2);
        var mockGame = mock(it.polimi.ingsw.galaxytruckers.client.model.Game.class);
        when(mockGame.getObservers()).thenReturn(java.util.Collections.emptyList());
        state.game = mockGame;
        state.notifyChoosePlanet(myShip, 1, nextShip);
        assertFalse(state.isMyTurn());
        assertEquals(myShip, state.getOptions()[1]);
    }

    @Test
    void notifyChoosePlanetNoChoice() {
        TestShipBoard myShip = new TestShipBoard(GameColor.BLUE);
        ChoosePlanetState state = new ChoosePlanetState(myShip, myShip, 2);
        var mockGame = mock(it.polimi.ingsw.galaxytruckers.client.model.Game.class);
        when(mockGame.getObservers()).thenReturn(java.util.Collections.emptyList());
        state.game = mockGame;
        state.notifyChoosePlanet(myShip, -1, myShip);
        assertNull(state.getOptions()[0]);
    }

    @Test
    void notifyCurrentPlayerUpdateSetsTurn() {
        TestShipBoard myShip = new TestShipBoard(GameColor.BLUE);
        TestShipBoard otherShip = new TestShipBoard(GameColor.RED);
        ChoosePlanetState state = new ChoosePlanetState(myShip, otherShip, 1);
        var mockGame = mock(it.polimi.ingsw.galaxytruckers.client.model.Game.class);
        when(mockGame.getObservers()).thenReturn(java.util.Collections.emptyList());
        state.game = mockGame;
        state.notifyCurrentPlayerUpdate(myShip);
        assertTrue(state.isMyTurn());
        state.notifyCurrentPlayerUpdate(otherShip);
        assertFalse(state.isMyTurn());
    }

    @Test
    void getOptionsAndSetOptions() {
        TestShipBoard myShip = new TestShipBoard(GameColor.BLUE);
        ChoosePlanetState state = new ChoosePlanetState(myShip, myShip, 2);
        ShipBoard[] newOptions = {myShip, null};
        state.setOptions(newOptions);
        assertEquals(myShip, state.getOptions()[0]);
        assertNull(state.getOptions()[1]);
    }
}