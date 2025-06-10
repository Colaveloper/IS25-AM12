package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;

public non-sealed class HandleProjectileState extends ActivateState implements GameStateInterface{
    Projectile projectile;

    public HandleProjectileState(ShipBoard shipBoard, Projectile projectile) {
        super(shipBoard, projectile.getActivatablePoints(shipBoard));
        this.projectile = projectile;
    }

    @Override
    public void activateComponent(ShipBoard shipBoard, Point position) {
        if (batteriesToSpend == 0) {
            super.activateComponent(shipBoard, position);
        } else {
            throw new IllegalStateException("You already activated a component");
        }
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if(this.shipBoard != shipBoard){
            throw new IllegalStateException("It's not your turn");
        }
        if (batteriesToSpend > 0) {
            throw new IllegalStateException("You still have batteries to spend");
        }
        if (projectile.fireAt(shipBoard)) {
            List<Set<Point>> shipPieces = shipBoard.getConnectedSets();
            if (shipPieces.size() > 1) {
                game.setCurrentState(new ChooseShipPieceState(shipPieces, shipBoard));
                return;
            }
        }
        game.setCurrentState(getNextState());
    }

    public Projectile getProjectile() {
        return projectile;
    }
}
