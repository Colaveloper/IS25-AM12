package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class CurrentProjectile extends Physical{
    private int projectileRoll;
    private int projectileDirection;
    private ProjectileType projectileType;

    private StringBuilder description;

    public CurrentProjectile(ProjectileType projectileType, int projectileDirection, int projectileRoll ) {
        this.projectileRoll = projectileRoll;
        this.projectileDirection = projectileDirection;
        this.projectileType = projectileType;
    }

    @Override
    public List<String> getDescription() {
        description = new StringBuilder();
        description.append("a ");
        switch (projectileType) {
            case ProjectileType.BIGFIRE:
                description.append("big cannon fire ");
                break;
            case ProjectileType.BIGMETEOR:
                description.append("big meteor ");
                break;
            case ProjectileType.SMALLFIRE:
                description.append("small cannon fire ");
                break;
            case ProjectileType.SMALLMETEOR:
                description.append("small meteor ");
                break;
        }
        description.append("is approaching on ");
        switch (projectileDirection) {
            case 0:
                description.append("column " + projectileRoll + " from the front!");
                break;
            case 1:
                description.append("row " + projectileRoll + " from the left!");
                break;
            case 2:
                description.append("column " + projectileRoll + " from the back!");
                break;
            case 3:
                description.append("row " + projectileRoll + " from the right!");
                break;
        }

        List<String> result = new ArrayList<>();
        result.add(description.toString());

        return result;
    }
}