package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

public record Projectile(int roll, Direction direction, ProjectileType type) {}
