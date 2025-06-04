package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

public record Projectile(int roll, it.polimi.ingsw.galaxytruckers.view.Direction direction, ProjectileType type) {}
