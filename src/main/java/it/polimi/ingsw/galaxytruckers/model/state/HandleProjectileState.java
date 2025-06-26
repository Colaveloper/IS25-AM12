package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Represents the state of the game where a player can handle a projectile.
 * The player can activate components or spend batteries to defend from the projectile.
 */
public non-sealed class HandleProjectileState extends ActivateState implements GameStateInterface {
    private final Projectile projectile;

    /**
     * Constructor for HandleProjectileState. Sets the available positions
     * of activatable components to the positions of the activatable components on the ship board
     * that can be used to defend from the projectile.
     *
     * @param shipBoard  the ship board of the player handling the projectile
     * @param projectile the projectile being handled
     */
    public HandleProjectileState(ShipBoard shipBoard, Projectile projectile) {
        super(shipBoard, projectile.getActivatablePoints(shipBoard));
        this.projectile = projectile;
    }

    /**
     * {@inheritDoc}
     * Moves to the next state if the player already activated a battery for the component.
     *
     * @param shipBoard the ship board on which the component is located
     * @param position  the position of the component to activate
     * @throws IllegalStateException if the player has already activated a component
     */
    @Override
    public synchronized void activateComponent(ShipBoard shipBoard, Point position) {
        if (batteriesToSpend >= 1) {
            throw new IllegalStateException("You already activated a component");
        }
        super.activateComponent(shipBoard, position);
        if (batteriesToSpend == 0) endStateAction();
    }

    /**
     * {@inheritDoc}
     * Moves to the next state if the player already activated a component to spend
     * the battery on.
     *
     * @param shipBoard the ship board where the battery is used
     * @param point     the point on the ship board where the battery is located
     * @throws IllegalStateException if the player has already spent a battery
     */
    @Override
    public synchronized void spendBatteries(ShipBoard shipBoard, Point point) {
        if (batteriesToSpend <= -1) {
            throw new IllegalStateException("You already spent a battery");
        }
        super.spendBatteries(shipBoard, point);
        if (batteriesToSpend == 0) endStateAction();
    }

    /**
     * {@inheritDoc}
     * The projectile is fired at the ship board. If the ship board is not in one piece
     * afterward, the game transitions to a {@link ChooseShipPieceState}
     */
    @Override
    protected void endStateAction() {
        if (projectile.fireAt(shipBoard)) {
            List<Set<Point>> shipPieces = shipBoard.getConnectedSets();
            if (shipPieces.size() > 1) {
                expired = true;
                game.submitStateTransition(() ->
                        game.setCurrentState(new ChooseShipPieceState(shipPieces, shipBoard))
                );
                return;
            }
        }
        getNextState();
    }

    /**
     * @return the projectile being handled in this state.
     */
    public synchronized Projectile getProjectile() {
        return projectile;
    }
}
