package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.util.List;
import java.util.Set;

import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import static org.junit.jupiter.api.Assertions.*;

class AdventureStateTest {
    static class TestAdventureState extends AdventureState {
        public TestAdventureState() {
            this.currentShip = null;
        }
    }

    private TestAdventureState state;
    private Game game;
    private ShipBoard shipBoard;
    private AdventureCard card;

    @BeforeEach
    void setUp() {
        state = new TestAdventureState();
        game = Mockito.mock(Game.class);
        shipBoard = Mockito.mock(ShipBoard.class);
        card = Mockito.mock(AdventureCard.class);
        state.game = game;
        state.currentShip = shipBoard;
    }

    @Test
    void getImOut() {
        state.imOut = false;
        assertFalse(state.getImOut());
        state.imOut = true;
        assertTrue(state.getImOut());
    }

    @Test
    void getCurrentCard() {
        Mockito.when(game.getCurrentCard()).thenReturn(card);
        assertEquals(card, state.getCurrentCard());
    }

    @Test
    void getAvailableActions() {
        state.imOut = false;
        assertEquals(List.of(StateActions.GIVE_UP), state.getAvailableActions());
        state.imOut = true;
        assertEquals(List.of(), state.getAvailableActions());
    }

    @Test
    void setGame() {
        ShipBoard myShip = Mockito.mock(ShipBoard.class);
        state.myShip = myShip;
        Mockito.when(game.getGivenUpShips()).thenReturn(Set.of(myShip));
        state.setGame(game);
        assertTrue(state.imOut);
        Mockito.when(game.getGivenUpShips()).thenReturn(Set.of());
        state.setGame(game);
        assertFalse(state.imOut);
    }

    @Test
    void notifyLoseCrew() {
        Point p = new Point(1, 2);
        var observer = Mockito.mock(ModelObserver.class);
        Mockito.when(game.getObservers()).thenReturn(List.of(observer));
        state.game = game;
        state.notifyLoseCrew(shipBoard, p);
        Mockito.verify(shipBoard).loseCrew(p);
        Mockito.verify(observer).notifyLoseCrew(shipBoard, p);
    }

    @Test
    void getShipBoard() {
        assertEquals(shipBoard, state.getShipBoard());
    }
}