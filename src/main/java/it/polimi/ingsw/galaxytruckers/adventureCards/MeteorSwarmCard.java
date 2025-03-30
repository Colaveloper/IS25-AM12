package it.polimi.ingsw.galaxytruckers.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.state.ChooseShipPieceState;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MeteorSwarmCard extends AdventureCard {
    //attributes
    private final List<Projectile> projectiles;
    private Projectile currentProjectile;


    public MeteorSwarmCard(Image image, Level level, FlightBoard flightBoard, List<Projectile> projectiles) {
        super(image, level, flightBoard);
        this.projectiles = new LinkedList<>(projectiles).reversed();
        this.currentProjectile = this.projectiles.removeLast();
    }

    @Override
    public GameState nextStep() {
        if (currentShipBoard != null) {  // There is a previous player that has to be hit //
            if (currentProjectile.fireAt(currentShipBoard)) {  // If a component is removed I need to check shipConnection
                List<Set<Point>> shipPieces = currentShipBoard.getConnectedSets();
                if (shipPieces.size() > 1) {
                    ShipBoard tempShipBoard = currentShipBoard;
                    currentShipBoard = null;
                    return new ChooseShipPieceState(tempShipBoard.getConnectedSets(), currentShipBoard);
                }
            }
        }
        // Letting the currentPlayer activate double cannons
        if (currentPlayerIndex < flightBoard.getOrderedShips().size()) {  // There are other players to evaluate
            currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            currentPlayerIndex++;
            Set<Point> availablePositions = new HashSet<>(currentShipBoard.getCannons().keySet());
            availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
            return new ActivateState(availablePositions, currentShipBoard); // Let the player activate cannon
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