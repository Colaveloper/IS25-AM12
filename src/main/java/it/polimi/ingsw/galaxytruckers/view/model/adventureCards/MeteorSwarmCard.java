package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.ArrayList;
import java.util.List;

public class MeteorSwarmCard extends AdventureCard {
    //attributes
    private final List<Projectile> projectiles;

    public MeteorSwarmCard(Game game, Level level, List<Projectile> projectiles, int id) {
        super(level, id);
        this.projectiles = new ArrayList<>(projectiles);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }
}