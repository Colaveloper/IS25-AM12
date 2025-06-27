package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the state where players initialize cabins on their ships in the Galaxy Truckers game.
 * This state is triggered after ship building and validation, allowing players to assign
 * crew members to cabins based on crew type requirements. Different types of crew members
 * can only be assigned to specific cabin positions on the ship.
 */
public final class ShipInitializationState extends GameState implements GameStateInterface {
    /** Mapping of ship boards to their available crew types and valid cabin positions */
    private final Map<ShipBoard, Map<CrewType,Set<Point>>> crewtypeToPoints;

    /**
     * Creates a new ShipInitializationState with the specified parameters.
     * Initializes the state with the player's ship and the mapping of ship boards
     * to available crew types and valid cabin positions.
     *
     * @param myShip The ship board of the local player
     * @param crewtypeToPoints Mapping of ship boards to their crew types and valid cabin positions
     */
    public ShipInitializationState(ShipBoard myShip, Map<ShipBoard, Map<CrewType, Set<Point>>> crewtypeToPoints) {
        this.myShip = myShip;
        this.crewtypeToPoints = crewtypeToPoints;
    }

    @Override
    public List<StateActions> getAvailableActions() {

        //no need to check for conditional actions, it's not possible to check if it's my turn so the check is in the screen
        return List.of(
                StateActions.INITIALIZE_CABIN,
                StateActions.GO_NEXT
        );
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        int numResidents = shipBoard.initializeCabin(point, crewType);
        crewtypeToPoints.get(shipBoard).remove(crewType);
        game.getObservers().forEach(observer -> observer.notifyInitializeCabin(shipBoard, point, crewType, numResidents));
    }

    /**
     * Gets the mapping of ship boards to their available crew types and valid cabin positions.
     * This mapping indicates which crew types can be assigned to which positions on each ship.
     * When a crew type is assigned to a cabin, it is removed from this mapping.
     *
     * @return Mapping of ship boards to their crew types and valid cabin positions
     */
    public Map<ShipBoard, Map<CrewType, Set<Point>>> getCrewtypeToPoints() {
        return crewtypeToPoints;
    }
}
