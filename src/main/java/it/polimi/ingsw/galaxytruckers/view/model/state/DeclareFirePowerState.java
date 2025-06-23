package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the state where players declare and activate firepower in the Galaxy Truckers game.
 * This state allows players to activate cannon components on their ship to determine
 * their combat strength. Only cannons that are in the activatable components map can be used.
 * <p>
 * This is a specific implementation of the ActivateState focused on cannon activation.
 * </p>
 */
public final class DeclareFirePowerState extends ActivateState {

    /**
     * Creates a new DeclareFirePowerState for firepower declaration.
     * Initializes the state with the player's ship and the currently active ship.
     * The available positions for activation are determined by filtering the ship's
     * cannons to include only those that are currently activatable.
     *
     * @param myShip The ship board of the local player
     * @param shipBoard The ship board that is currently active
     */
    public DeclareFirePowerState(ShipBoard myShip, ShipBoard shipBoard) {
        super(myShip, shipBoard, shipBoard.getCannons().keySet().stream()
                .filter(p -> shipBoard.getActivatables().containsKey(p))
                .collect(Collectors.toSet()));
    }
}
