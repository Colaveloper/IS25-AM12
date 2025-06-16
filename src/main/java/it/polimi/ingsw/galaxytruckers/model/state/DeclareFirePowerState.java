package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;

public final class DeclareFirePowerState extends ActivateState implements GameStateInterface{
    int currentFirePower;

    public DeclareFirePowerState(ShipBoard shipBoard) {
        super(shipBoard, new HashSet<>(shipBoard.getActivatables().keySet()));
        this.availablePositions.retainAll(shipBoard.getCannons().keySet());
        this.currentFirePower = shipBoard.getFirePower();
    }

    @Override
    public synchronized void activateComponent(ShipBoard shipBoard, Point position) {
        super.activateComponent(shipBoard, position);
        currentFirePower = shipBoard.getFirePower();
    }
}
