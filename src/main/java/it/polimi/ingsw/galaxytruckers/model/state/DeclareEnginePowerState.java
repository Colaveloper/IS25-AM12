package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;

/**
 * Represents the state where the player declares the engine power of their ship.
 */
public final class DeclareEnginePowerState extends ActivateState implements GameStateInterface {

    /**
     * Constructor for DeclareEnginePowerState. Sets the available positions of
     * activatable components to the positions of the activatable engines on the ship board.
     *
     * @param shipBoard the ship board of the player who is declaring engine power
     */
    public DeclareEnginePowerState(ShipBoard shipBoard) {
        super(shipBoard, new HashSet<>(shipBoard.getActivatables().keySet()));
        this.availablePositions.retainAll(shipBoard.getEngines().keySet());
    }
}
