package it.polimi.ingsw.galaxytruckers.server.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.server.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.HandleProjectileState;

import java.util.List;
import java.util.Optional;

/**
 * Represents a penalty that involves handling a projectile threat.
 * This class implements the Penalty interface and is used to manage the projectiles
 * that threaten the player's ship during the adventure.
 */
public class ProjectileThreat implements Penalty {
    private final List<Projectile> projectiles;

    public ProjectileThreat(List<Projectile> projectiles) {
        this.projectiles = projectiles.reversed();
    }

    /**
     * Inflicts a penalty by handling the next projectile threat for the player's ship.
     * If there are no projectiles left, no penalty is applied.
     *
     * @param shipBoard the ShipBoard of the player being penalized
     * @param flightBoard the FlightBoard containing all players' ship boards
     * @return an Optional containing a HandleProjectileState if a projectile is to be handled, or empty if not
     */
    @Override
    public Optional<AdventureState> inflictPenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        if (projectiles.isEmpty()) {
            return Optional.empty();
        } else {
            Projectile projectile = projectiles.removeLast();
            return Optional.of(new HandleProjectileState(shipBoard, projectile));
        }
    }
}
