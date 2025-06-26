package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.Set;

/**
 * DTO for the HandleProjectile state.
 *
 * @param playerName      the name of the player who is handling the projectile
 * @param projectileType  the type of projectile being handled
 * @param diceRoll        the dice roll result for the projectile action
 * @param direction       the direction from which the projectile is coming
 * @param availablePoints the set of points available for the player to choose
 *                        from when handling the projectile
 */
public record HandleProjectileDTO(
        String playerName,
        ProjectileType projectileType,
        int diceRoll,
        Direction direction,
        Set<Point> availablePoints
) implements StateDTO, ComplexStateDTO {
}
