package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.util.List;

public record ProjectileEvent(String playerName, ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) implements Event {
    public static ProjectileEvent from(ShipBoard shipBoard, Projectile projectile) {
        return new ProjectileEvent(
                Player.getPlayer(shipBoard).getNickname(),
                null, //TODO: add projectile type as a shared enum and bind it to server model classes
                0, //TODO: add getter for projectile direction
                0, //TODO: add getter for projectile dice roll
                projectile.getActivatablePoints(shipBoard).stream().toList(),
                shipBoard.getBatteries().keySet().stream().toList()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
