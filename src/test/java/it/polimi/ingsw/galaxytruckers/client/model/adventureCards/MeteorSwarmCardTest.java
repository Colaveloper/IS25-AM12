package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.shared.enums.ProjectileType;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeteorSwarmCardTest {
    @Test
    void getProjectilesReturnsCorrectProjectiles() {
        Projectile p1 = new Projectile(3, Direction.UP, ProjectileType.BIGMETEOR);
        Projectile p2 = new Projectile(5, Direction.DOWN, ProjectileType.SMALLFIRE);
        List<Projectile> projectiles = List.of(p1, p2);
        MeteorSwarmCard card = new MeteorSwarmCard(Level.FIRST, projectiles, 10);
        assertEquals(projectiles, card.getProjectiles());
    }

    @Test
    void getProjectilesEmptyList() {
        MeteorSwarmCard card = new MeteorSwarmCard(Level.SECOND, List.of(), 11);
        assertTrue(card.getProjectiles().isEmpty());
    }

    @Test
    void testInheritanceAndId() {
        MeteorSwarmCard card = new MeteorSwarmCard(Level.TEST, List.of(), 99);
        assertEquals(Level.TEST, card.getCardLevel());
        assertEquals(99, card.getId());
    }
}