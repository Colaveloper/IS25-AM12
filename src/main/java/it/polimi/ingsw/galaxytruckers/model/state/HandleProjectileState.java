package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;

public non-sealed class HandleProjectileState extends ActivateState implements GameStateInterface{
    private final Projectile projectile;

    public HandleProjectileState(ShipBoard shipBoard, Projectile projectile) {
        super(shipBoard, projectile.getActivatablePoints(shipBoard));
        this.projectile = projectile;
    }

    @Override
    public synchronized void activateComponent(ShipBoard shipBoard, Point position) {
        if (batteriesToSpend >= 1) {
            throw new IllegalStateException("You already activated a component");
        }
        super.activateComponent(shipBoard, position);
        if (batteriesToSpend == 0) endStateAction();
    }

    @Override
    public synchronized void spendBatteries(ShipBoard shipBoard, Point point) {
        if (batteriesToSpend <= -1) {
            throw new IllegalStateException("You already spent a battery");
        }
        super.spendBatteries(shipBoard, point);
        if (batteriesToSpend == 0) endStateAction();
    }

    @Override
    protected void endStateAction() {
        if (projectile.fireAt(shipBoard)) {
            List<Set<Point>> shipPieces = shipBoard.getConnectedSets();
            if (shipPieces.size() > 1) {
                expired = true;
                game.submitStateTransition(() ->
                        game.setCurrentState(new ChooseShipPieceState(shipPieces,shipBoard))
                );
                return;
            }
        }
        getNextState();
    }

    public synchronized Projectile getProjectile() {
        return projectile;
    }
}
