package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.server.model.state.HandleProjectileState;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.util.LinkedList;
import java.util.List;

public class MeteorSwarmCard extends AdventureCard {
    //attributes
    private final List<Projectile> projectiles;
    private Projectile currentProjectile;

    /**
     * Constructs a MeteorSwarmCard.
     *
     * @param game the game instance
     * @param level the adventure card level
     * @param projectiles the list of projectiles to be handled
     * @param id the unique card identifier
     */
    public MeteorSwarmCard(Game game, Level level, List<Projectile> projectiles, int id) {
        super(game, level, id);
        this.projectiles = new LinkedList<>(projectiles).reversed();
    }

    @Override
    public void initialize() {
        super.initialize();
        this.currentProjectile = this.projectiles.removeLast();
    }

    @Override
    public AdventureState getNextState() {
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
                return getNextState();
            }
        }
    }

    @VisibleForTesting
    public ShipBoard getCurrentShipBoard() {
        return currentShipBoard;
    }
}