package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HandleProjectileStateTest {
    private ShipBoard shipBoard;
    private Point point;
    private HandleProjectileState state;
    private Projectile projectile;
    private ModelObserver observer;

    @BeforeEach
    void setUp() {
        shipBoard = mock(ShipBoard.class);
        point = new Point(1, 2);
        projectile = mock(Projectile.class);
        Game game = mock(Game.class);
        observer = mock(ModelObserver.class);
        when(game.getObservers()).thenReturn(List.of(observer));
        state = new HandleProjectileState(shipBoard, shipBoard, projectile, Set.of(point));
        state.game = game;
    }

    @Test
    void testNotifyRemoveComponent() {
        state.notifyRemoveComponent(shipBoard, point);
        verify(shipBoard).removeComponent(point);
        verify(shipBoard).incrementLosses(1);
        verify(observer).notifyRemoveComponent(shipBoard, point);
    }

    @Test
    void testGetProjectile() {
        Projectile result = state.getProjectile();
        assertEquals(projectile, result, "getProjectile should return the projectile passed to the constructor");
    }
}