package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

public final class HandleProjectileState extends ActivateState {
    private final Projectile projectile;

    public HandleProjectileState(ShipBoard myShip, ShipBoard shipBoard, Projectile projectile, Set<Point> availablePositions) {
        super(myShip, shipBoard, availablePositions);
        this.projectile = projectile;
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        shipBoard.removeComponent(point);
        shipBoard.incrementLosses(1);
        game.getObservers().forEach(observer -> observer.notifyRemoveComponent(shipBoard, point));
    }

    public Projectile getProjectile() {
        return projectile;
    }
}
