package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;

public final class DeclareEnginePowerState extends ActivateState implements GameStateInterface {
    int enginePower;

    public DeclareEnginePowerState(ShipBoard shipBoard) {
        super(shipBoard, new HashSet<>(shipBoard.getActivatables().keySet()));
        this.availablePositions.retainAll(shipBoard.getEngines().keySet());
        this.enginePower = shipBoard.getEnginePower();
    }

    @Override
    public synchronized void activateComponent(ShipBoard shipBoard, Point position) {
        super.activateComponent(shipBoard, position);
        this.enginePower = shipBoard.getEnginePower();
    }
}
