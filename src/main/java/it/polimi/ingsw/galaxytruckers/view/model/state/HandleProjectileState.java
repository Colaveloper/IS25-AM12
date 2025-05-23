package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

public class HandleProjectileState extends ActivateState {
    private final Projectile projectile;

    public HandleProjectileState(ShipBoard shipBoard, Projectile projectile, Set<Point> availablePositions) {
        super(shipBoard, availablePositions);
        this.projectile = projectile;
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        shipBoard.removeComponent(point);
    }

    public Projectile getProjectile() {
        return projectile;
    }
}
