package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.state.ChooseShipPieceState;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MeteorSwarmCard extends AdventureCard {
    //attributes
    private List<Projectile> projectiles;
    private Projectile currentProjectile;


    public MeteorSwarmCard(Image image, Level level, FlightBoard flightBoard, List<Projectile> projectiles) {
        super(image, level, flightBoard);
        this.projectiles = projectiles; // list is inverted to be treated as a stack
        this.currentProjectile = projectiles.removeFirst();
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
        if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {  // There are other players to evaluate
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
                currentProjectile = projectiles.removeFirst();
                return nextStep();
            }
        }


    }
    //USED METHODS


}