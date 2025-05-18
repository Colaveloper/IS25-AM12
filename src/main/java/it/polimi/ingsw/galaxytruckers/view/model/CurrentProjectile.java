package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

public record CurrentProjectile(int roll, int direction, ProjectileType type) {
}
