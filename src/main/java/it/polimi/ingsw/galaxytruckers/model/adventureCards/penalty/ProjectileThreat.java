package it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.HandleProjectileState;

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
