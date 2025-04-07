package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class HandleProjectileState extends ActivateState{
    Projectile projectile;

    public HandleProjectileState(ShipBoard shipBoard, Projectile projectile) {
        super(shipBoard);
        this.projectile = projectile;
        this.availablePositions = projectile.getActivatablePoints(shipBoard);
    }

    @Override
    public void activateComponent(Point position) {
        if (batteriesToSpend == 0) {
            super.activateComponent(position);
        } else {
            throw new IllegalStateException("You already activated a component");
        }
    }

    @Override
    public void goNext() {
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
        game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
    }
}
