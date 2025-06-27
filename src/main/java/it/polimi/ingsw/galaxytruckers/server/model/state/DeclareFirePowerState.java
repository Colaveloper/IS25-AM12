package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.util.HashSet;

/**
 * Represents the state of the game where a player can declare their firepower.
 */
public final class DeclareFirePowerState extends ActivateState implements GameStateInterface{

    /**
     * Constructor for DeclareFirePowerState. Sets the available positions
     * of activatable components to the positions of the activatable cannons on the ship board.
     * @param shipBoard the ship board of the player declaring firepower
     */
    public DeclareFirePowerState(ShipBoard shipBoard) {
        super(shipBoard, new HashSet<>(shipBoard.getActivatables().keySet()));
        this.availablePositions.retainAll(shipBoard.getCannons().keySet());
    }
}
