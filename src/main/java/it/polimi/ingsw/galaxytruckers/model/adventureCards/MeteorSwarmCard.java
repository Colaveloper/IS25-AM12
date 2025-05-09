package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;

import java.util.LinkedList;
import java.util.List;

public class MeteorSwarmCard extends AdventureCard {
    //attributes
    private final List<Projectile> projectiles;
    private Projectile currentProjectile;


    public MeteorSwarmCard(Game game, List<Projectile> projectiles) {
        super(game);
        this.projectiles = new LinkedList<>(projectiles).reversed();
    }

    @Override
    public void initialize() {
        super.initialize();
        this.currentProjectile = this.projectiles.removeLast();
    }

    @Override
    public GameState nextStep() {
        // Letting the currentPlayer activate double cannons
        if (currentShipBoard != null) {
            currentShipBoard.deactivateAll();
        }
        if (currentPlayerIndex < flightBoard.getOrderedShips().size()) {  // There are other players to evaluate
            currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            currentPlayerIndex++;
            return new HandleProjectileState(currentShipBoard, currentProjectile); // Let the player activate cannon
        }
        else {
            if (projectiles.isEmpty()) {
                return new DrawCardState();
            } else {
                currentProjectile = projectiles.removeLast();
                currentShipBoard = null;
                currentPlayerIndex = 0;
                return nextStep();
            }
        }
    }

    @VisibleForTesting
    public ShipBoard getCurrentShipBoard() {
        return currentShipBoard;
    }
}