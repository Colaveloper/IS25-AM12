package it.polimi.ingsw.galaxytruckers.client.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.shared.enums.ProjectileType;

public record Projectile(int roll, Direction direction, ProjectileType type) {}
