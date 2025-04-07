package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;

public class DeclareFirePowerState extends ActivateState {
    int currentFirePower;

    public DeclareFirePowerState(ShipBoard shipBoard) {
        super(shipBoard);
        this.availablePositions = new HashSet<>(shipBoard.getActivatables().keySet());
        this.availablePositions.retainAll(shipBoard.getCannons().keySet());
        this.currentFirePower = shipBoard.getFirePower();
    }

    @Override
    public void activateComponent(Point position) {
        super.activateComponent(position);
        currentFirePower = shipBoard.getFirePower();
    }
}
