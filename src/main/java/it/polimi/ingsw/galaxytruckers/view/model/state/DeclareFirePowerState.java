package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

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
}
