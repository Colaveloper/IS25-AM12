package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;

import java.util.List;

public final class ProjectileThreat implements Penalty {
    private List<Projectile> projectiles;

    public ProjectileThreat(List<Projectile> projectiles) {
        this.projectiles = projectiles.reversed();
    }
}
