package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import static org.junit.jupiter.api.Assertions.*;

class PiratesCardTest {

    @Test
    void getFirePowerThreshold() {
        PiratesCard card = new PiratesCard(Level.FIRST, 7, 10, 2, List.of(), 42);
        assertEquals(7, card.getFirePowerThreshold());
    }

    @Test
    void getProjectiles() {
        Projectile p1 = new Projectile(1, Direction.UP, ProjectileType.BIGMETEOR);
        Projectile p2 = new Projectile(2, Direction.DOWN, ProjectileType.SMALLFIRE);
        List<Projectile> input = Arrays.asList(p1, p2);
        PiratesCard card = new PiratesCard(Level.FIRST, 7, 10, 2, input, 42);
        // projectiles should be reversed
        List<Projectile> expected = Arrays.asList(p2, p1);
        assertEquals(expected, card.getProjectiles());
    }

    @Test
    void getCreditPrize() {
        PiratesCard card = new PiratesCard(Level.FIRST, 7, 15, 2, List.of(), 42);
        assertEquals(15, card.getCreditPrize());
    }

    @Test
    void getFlightDaysLoss() {
        PiratesCard card = new PiratesCard(Level.FIRST, 7, 10, 5, List.of(), 42);
        assertEquals(5, card.getFlightDaysLoss());
    }
}