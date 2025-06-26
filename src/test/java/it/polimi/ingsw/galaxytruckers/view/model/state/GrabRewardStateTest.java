package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GrabRewardStateTest {
    @Test
    void testGetAvailableActionsMyShipIsCurrentShip() {
        ShipBoard myShip = mock(ShipBoard.class);
        GrabRewardState state = new GrabRewardState(myShip, myShip);
        List<StateActions> actions = state.getAvailableActions();
        assertTrue(actions.contains(StateActions.GRAB_REWARD));
        assertTrue(actions.contains(StateActions.GO_NEXT));
    }

    @Test
    void testGetAvailableActionsMyShipIsNotCurrentShip() {
        ShipBoard myShip =mock(ShipBoard.class);
        ShipBoard currentShip =mock(ShipBoard.class);
        GrabRewardState state = new GrabRewardState(myShip, currentShip);
        List<StateActions> actions = state.getAvailableActions();
        assertFalse(actions.contains(StateActions.GRAB_REWARD));
        assertFalse(actions.contains(StateActions.GO_NEXT));
    }

    @Test
    void testNotifyGrabRewardSetsCreditsOnCurrentShip() {
        ShipBoard myShip =mock(ShipBoard.class);
        ShipBoard currentShip =mock(ShipBoard.class);
        when(currentShip.getCredits()).thenReturn(42);
        GrabRewardState state = new GrabRewardState(myShip, currentShip);
        int credits = 42;
        state.notifyGrabReward(currentShip, credits);
        assertEquals(credits, currentShip.getCredits());
        assertEquals(0, myShip.getCredits());
    }
}