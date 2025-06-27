package it.polimi.ingsw.galaxytruckers.client.model.adventureCards.penalty;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.Projectile;

import java.util.List;

/**
 * Represents a penalty involving a threat from projectiles in the game.
 * Implements the {@link Penalty} interface.
 */
public final class ProjectileThreat implements Penalty {
    @VisibleForTesting
    private List<Projectile> projectiles;

    /**
     * Constructs a ProjectileThreat penalty with the specified list of projectiles.
     * The list is reversed upon construction.
     *
     * @param projectiles the list of projectiles representing the threat
     */
    public ProjectileThreat(List<Projectile> projectiles) {
        this.projectiles = projectiles.reversed();
    }
}
