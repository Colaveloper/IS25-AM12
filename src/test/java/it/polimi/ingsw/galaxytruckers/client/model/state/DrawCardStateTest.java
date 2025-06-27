package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.client.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.client.model.Game;
import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DrawCardStateTest {

    @Test
    void testGetAvailableActions() {
        ShipBoard myShip = mock(ShipBoard.class);
        DrawCardState leaderState = new DrawCardState(myShip, myShip);
        List<StateActions> actionsLeader = leaderState.getAvailableActions();
        assertTrue(actionsLeader.contains(StateActions.DRAW_CARD));
        ShipBoard otherShip = mock(ShipBoard.class);
        DrawCardState nonLeaderState = new DrawCardState(myShip, otherShip);
        List<StateActions> actionsNonLeader = nonLeaderState.getAvailableActions();
        assertFalse(actionsNonLeader.contains(StateActions.DRAW_CARD));
    }

    @Test
    void testGetAvailableActionsWithHasDrawn() {
        ShipBoard myShip = mock(ShipBoard.class);
        DrawCardState leaderState = new DrawCardState(myShip, myShip);
        leaderState.setHasDrawn(true);
        List<StateActions> actionsLeader = leaderState.getAvailableActions();
        assertTrue(actionsLeader.contains(StateActions.GO_NEXT));
        assertFalse(actionsLeader.contains(StateActions.DRAW_CARD));
    }

    @Test
    void testNotifyDrawCard() {
        ShipBoard myShip = mock(ShipBoard.class);
        DrawCardState state = new DrawCardState(myShip, myShip);
        var game = mock(Game.class);
        var observer = mock(ModelObserver.class);
        var card = mock(AdventureCard.class);
        when(game.getObservers()).thenReturn(List.of(observer));
        state.setGame(game);
        state.notifyDrawCard(card);
        verify(game).setCurrentCard(card);
        verify(observer).notifyDrawCard(card);
        assertTrue(state.hasDrawn());
    }
}
