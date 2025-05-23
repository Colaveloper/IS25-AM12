package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;

public class DeclareEnginePowerState extends ActivateState {
    int enginePower;

    public DeclareEnginePowerState(ShipBoard shipBoard) {
        super(shipBoard);
        this.availablePositions = new HashSet<>(shipBoard.getActivatables().keySet());
        this.availablePositions.retainAll(shipBoard.getEngines().keySet());
        this.enginePower = shipBoard.getEnginePower();
    }
}
