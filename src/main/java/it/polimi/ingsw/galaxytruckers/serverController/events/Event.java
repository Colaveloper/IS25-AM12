package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;

import java.io.IOException;
import java.util.Optional;

/**
 * Game event signaling a specific state change in the whole application.
 * Includes methods to inspect what changed
 */
public abstract class Event {
    private final ShipBoard involvedShip;

    protected Event(ShipBoard involvedShip) {
        this.involvedShip = involvedShip;
    }

    protected Event() {
        this.involvedShip = null;
    } // all ships are involved

    /**
     * Returns a reference to the ship that is involved in the event, if there is one
     * @return {@code Optional<ShipBoard>} with a reference to the involved ship, if any
     */
    public Optional<ShipBoard> getInvolvedShip() {
        return Optional.ofNullable(involvedShip);
    }

    /**
     * Handles the game event as defined by {@code eventHandler}
     * @param eventHandler the {@code EventHandler} that should handle the event
     */
    public abstract void accept(EventHandler eventHandler) throws IOException;
}
