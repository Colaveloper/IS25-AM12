package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an adventure card for a meteor swarm event in the game.
 * Stores the list of projectiles associated with the meteor swarm.
 * Extends {@link AdventureCard} and implements {@link AdventureCardInterface}.
 */
public final class MeteorSwarmCard extends AdventureCard implements AdventureCardInterface {

    private final List<Projectile> projectiles;

    /**
     * Constructs a MeteorSwarmCard with the specified level, projectiles, and unique identifier.
     *
     * @param level the level of the card
     * @param projectiles the list of projectiles for the meteor swarm
     * @param id the unique identifier for the card
     */
    public MeteorSwarmCard(Level level, List<Projectile> projectiles, int id) {
        super(level, id);
        this.projectiles = new ArrayList<>(projectiles);
    }

    /**
     * Returns the list of projectiles associated with this meteor swarm card.
     *
     * @return the list of projectiles
     */
    public List<Projectile> getProjectiles() {
        return projectiles;
    }
}