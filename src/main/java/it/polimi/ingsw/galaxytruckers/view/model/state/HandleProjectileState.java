package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Set;

/**
 * Represents the state where players handle incoming projectiles in the Galaxy Truckers game.
 * This state is triggered when a ship encounters a projectile during flight and needs to
 * activate shields or handle the resulting damage. Only specific components can be activated
 * to defend against projectiles.
 */
public final class HandleProjectileState extends ActivateState {
    /** The projectile that the player's ship is currently handling */
    private final Projectile projectile;

    /**
     * Creates a new HandleProjectileState with the specified parameters.
     * Initializes the state with the player's ship, the currently active ship,
     * the incoming projectile, and the available positions for shield activation.
     *
     * @param myShip The ship board of the local player
     * @param shipBoard The ship board that is currently active
     * @param projectile The incoming projectile that needs to be handled
     * @param availablePositions Set of positions where shield components can be activated
     */
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

    /**
     * Gets the projectile that the ship is currently handling.
     * This projectile represents the incoming threat that the player must defend against.
     *
     * @return The projectile being handled in this state
     */
    public Projectile getProjectile() {
        return projectile;
    }
}
