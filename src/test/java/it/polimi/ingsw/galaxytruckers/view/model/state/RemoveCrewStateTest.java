package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RemoveCrewStateTest {
    @Test
    void testGetAvailableActionsCurrentShipIsMyShip() {
        ShipBoard myShip = mock(ShipBoard.class);
        RemoveCrewState state = new RemoveCrewState(myShip, 1, myShip);
        List<StateActions> actions = state.getAvailableActions();
        assertTrue(actions.contains(StateActions.LOSE_CREW), "Should contain LOSE_CREW");
        assertTrue(actions.contains(StateActions.GIVE_UP), "Should contain GIVE_UP");
    }

    @Test
    void testGetAvailableActionsCurrentShipIsNotMyShip() {
        ShipBoard myShip = mock(ShipBoard.class);
        ShipBoard otherShip = mock(ShipBoard.class);
        RemoveCrewState state = new RemoveCrewState(myShip, 1, otherShip);
        List<StateActions> actions = state.getAvailableActions();
        assertFalse(actions.contains(StateActions.LOSE_CREW), "Should not contain LOSE_CREW");
        assertTrue(actions.contains(StateActions.GIVE_UP), "Should contain GIVE_UP");
    }

    @Test
    void testNotifyLoseCrewCallsShipBoardAndObservers() {
        ShipBoard shipBoard = mock(ShipBoard.class);
        Point point = new Point(1, 2);
        ModelObserver observer1 = mock(ModelObserver.class);
        ModelObserver observer2 = mock(ModelObserver.class);
        Game game = mock(Game.class);
        when(game.getObservers()).thenReturn(List.of(observer1, observer2));
        RemoveCrewState state = new RemoveCrewState(shipBoard, 1, shipBoard);
        try {
            java.lang.reflect.Field gameField = state.getClass().getSuperclass().getSuperclass().getDeclaredField("game");
            gameField.setAccessible(true);
            gameField.set(state, game);
        } catch (Exception e) {
            fail("Failed to inject mock game: " + e.getMessage());
        }
        state.notifyLoseCrew(shipBoard, point);
        verify(shipBoard).loseCrew(point);
        verify(observer1).notifyLoseCrew(shipBoard, point);
        verify(observer2).notifyLoseCrew(shipBoard, point);
    }
}