package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.client.model.Game;
import it.polimi.ingsw.galaxytruckers.client.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChooseShipPieceStateTest {
    @Test
    void getAvailableActions() {
        ShipBoard ship1 = mock(ShipBoard.class);
        ShipBoard ship2 = mock(ShipBoard.class);
        List<Set<Point>> pieces = List.of(Set.of(new Point(0,0)));
        ChooseShipPieceState state1 = new ChooseShipPieceState(ship1, pieces, ship1);
        ChooseShipPieceState state2 = new ChooseShipPieceState(ship1, pieces, ship2);
        assertTrue(state1.getAvailableActions().contains(StateActions.CHOOSE_SHIP_PIECE));
        assertFalse(state2.getAvailableActions().contains(StateActions.CHOOSE_SHIP_PIECE));
    }

    @Test
    void getShipPieces() {
        ShipBoard ship = mock(ShipBoard.class);
        List<Set<Point>> pieces = List.of(Set.of(new Point(2,2)), Set.of(new Point(3,3)));
        ChooseShipPieceState state = new ChooseShipPieceState(ship, pieces, ship);
        assertEquals(pieces, state.getShipPieces());
    }

    // Helper to set private/protected field via reflection - AI generated method
    private static void setField(Object target, String fieldName, Object value) {
        Class<?> c = target.getClass();
        while (c != null) {
            try {
                var f = c.getDeclaredField(fieldName);
                f.setAccessible(true);
                f.set(target, value);
                return;
            } catch (NoSuchFieldException ignored) {
                c = c.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Field '" + fieldName + "' not found");
    }

    @Test
    void notifyChooseShipPiece() {
        ShipBoard ship = mock(ShipBoard.class);
        List<Set<Point>> pieces = List.of(Set.of(new Point(4,4)));
        ChooseShipPieceState state = new ChooseShipPieceState(ship, pieces, ship);
        ModelObserver observer = mock(ModelObserver.class);
        Game game = mock(Game.class);
        when(ship.removeShipPiece(anyList(), anyInt())).thenReturn(List.of(new Point(5,5)));
        when(game.getObservers()).thenReturn(List.of(observer));
        setField(state, "game", game);
        state.notifyChooseShipPiece(ship, 0);
        verify(ship).removeShipPiece(pieces, 0);
        verify(observer).notifyChooseShipPiece(ship, 0, List.of(new Point(5,5)));
    }
}