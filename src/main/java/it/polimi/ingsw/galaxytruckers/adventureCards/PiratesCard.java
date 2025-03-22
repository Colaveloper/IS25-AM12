package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.*;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PiratesCard extends AdventureCard {
    // Card Parameters
    private final int firePowerThreshold;
    private final List<Projectile> projectiles;
    private final int creditPrize;
    private final int flightDaysLoss;

    // Card state descriptors
    private final List<ShipBoard> defeatedPlayers;
    private int defeatedPlayerIndex; // current defeated player
    private boolean defeated;
    private ShipBoard currentShipBoard;
    private Projectile currentProjectile;

    public PiratesCard(Image image, Level level, int firePowerThreshold, int creditPrize, int flightDaysLoss, List<Projectile> projectiles) {
        super(image, level);
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.projectiles = projectiles.reversed();  // list is inverted to be treated as a stack
        this.defeatedPlayers = new ArrayList<>();
        this.defeatedPlayerIndex = 0;
        this.defeated = false;
        this.currentShipBoard = null;
    }

    @Override
    public GameState nextStep() {
        if (!defeated) {
            if (currentShipBoard != null) {  // There is a previous player who needs their firepower evaluated
                if (currentShipBoard.getFirePower() > firePowerThreshold) {  // player defeats the enemy
                    defeated = true;
                    defeatedPlayerIndex = 0;
                    return new ChoiceState();
                } else if (currentShipBoard.getFirePower() < firePowerThreshold) { // player is defeated
                    defeatedPlayers.add(currentShipBoard);
                }
            }
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {  // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;
                Set<Point> availablePositions = new HashSet<>(currentShipBoard.getCannons().keySet());
                availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
                return new ActivateState(availablePositions, currentShipBoard);
            } else {  // There are no more players and no one has defeated the enemy
                defeated = true;
                defeatedPlayerIndex = 0;
                return nextStep();
            }
        } else {
            if (currentShipBoard != null) {  // There is a previous player that has to be hit
                if (currentProjectile.fireAt(currentShipBoard)) {  // If a component is removed I need to check shipConnection
                    List<Set<Point>> shipPieces = currentShipBoard.getConnectedSets();
                    if (shipPieces.size() > 1) {
                        return new ChooseShipPieceState(currentShipBoard.getConnectedSets(), currentShipBoard);
                    }
                }
            }
            if (defeatedPlayerIndex < defeatedPlayers.size()) {  // There are still players that need to handle projectiles
                currentShipBoard = defeatedPlayers.get(defeatedPlayerIndex);
                defeatedPlayerIndex++;
                return new ActivateState(currentProjectile.getActivatablePoints(currentShipBoard), currentShipBoard);
            } else {  // There are no more players that need to handle projectiles
                if (projectiles.isEmpty()) {
                    return new DrawCardState();
                } else {
                    currentProjectile = projectiles.removeLast();
                    defeatedPlayerIndex = 0;
                    return nextStep();
                }
            }
        }
    }

    @Override
    public void choose(boolean choice) {
        currentShipBoard.gainCredits(creditPrize);
        flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
        currentShipBoard = null;
    }
}
