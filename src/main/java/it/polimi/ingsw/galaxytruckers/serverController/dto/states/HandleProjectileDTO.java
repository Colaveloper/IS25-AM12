package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.util.Set;

public record HandleProjectileDTO(String playerName, ProjectileType projectileType, int diceRoll, it.polimi.ingsw.galaxytruckers.view.Direction direction,
                                  Set<Point> availablePoints) implements StateDTO {
}
