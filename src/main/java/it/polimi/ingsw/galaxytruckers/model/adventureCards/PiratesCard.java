package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;

import java.util.ArrayList;
import java.util.List;

public class PiratesCard extends AdventureCard {
    // Card Parameters
    private final int firePowerThreshold;
    private final List<Projectile> projectiles;
    private final int creditPrize;
    private final int flightDaysLoss;

    // Card state descriptors
    private List<ShipBoard> defeatedPlayers;
    private boolean defeated;
    private Projectile currentProjectile;
    private ShipBoard winnerShipBoard;

    public PiratesCard(Game game, Level level, int firePowerThreshold, int creditPrize, int flightDaysLoss, List<Projectile> projectiles, int id) {
        super(game, level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.projectiles = projectiles.reversed();  // list is inverted to be treated as a stack
    }

    @Override
    public void initialize() {
        super.initialize();
        this.defeatedPlayers = new ArrayList<>();
        this.defeated = false;
        this.winnerShipBoard = null;
        this.currentProjectile = this.projectiles.removeLast();
    }

    @Override
    public GameState nextStep() {
        if (!defeated) { // Establishing winner and defeated players, if any
            // Evaluating previous player firepower, after double cannons activation
            if (currentShipBoard != null) {  // There is a previous player who needs their firepower evaluated
                int currentFirePower = currentShipBoard.getFirePower();
                currentShipBoard.deactivateAll();
                if (currentFirePower > firePowerThreshold) {  // player defeats the enemy
                    defeated = true;
                    currentPlayerIndex = 0;
                    winnerShipBoard = currentShipBoard;
                    currentShipBoard = null;
                    return new GrabRewardState(winnerShipBoard, this::getReward); // Let the player choose whether to collect the prize
                } else if (currentFirePower < firePowerThreshold) { // player is defeated
                    defeatedPlayers.add(currentShipBoard);
                }
            }
            // Letting the currentPlayer activate double cannons
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {  // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;
                return new DeclareFirePowerState(currentShipBoard); // Let the player activate double cannons
            } else {  // There are no more players and no one has defeated the enemy
                defeated = true;
                currentPlayerIndex = 0;
                currentShipBoard = null;
                return nextStep();
            }
        } else { // Firing at defeated players
            if (currentShipBoard != null) {
                currentShipBoard.deactivateAll();
            }
            if (currentPlayerIndex < defeatedPlayers.size()) {  // There are still players that need to handle projectiles
                currentShipBoard = defeatedPlayers.get(currentPlayerIndex);
                currentPlayerIndex++;
                return new HandleProjectileState(currentShipBoard, currentProjectile); // Let the player activate shields
            } else {  // There are no more players that need to handle projectiles
                if (projectiles.isEmpty()) {
                    return new DrawCardState();
                } else {
                    currentPlayerIndex = 0;
                    currentShipBoard = null;
                    currentProjectile = projectiles.removeLast();
                    return nextStep();
                }
            }
        }
    }

    public void getReward() {
        winnerShipBoard.gainCredits(creditPrize);
        flightBoard.displaceShip(winnerShipBoard, -flightDaysLoss);
    }

    @VisibleForTesting
    protected int getFirePowerThreshold() {
        return firePowerThreshold;
    }

    @VisibleForTesting
    protected List<Projectile> getProjectiles() {
        return projectiles;
    }

    @VisibleForTesting
    protected int getCreditPrize() {
        return creditPrize;
    }

    @VisibleForTesting
    protected int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    @VisibleForTesting
    protected List<ShipBoard> getDefeatedPlayers() {
        return defeatedPlayers;
    }

    @VisibleForTesting
    protected boolean isDefeated() {
        return defeated;
    }

    @VisibleForTesting
    protected Projectile getCurrentProjectile() {
        return currentProjectile;
    }

    @VisibleForTesting
    protected ShipBoard getWinnerShipBoard() {
        return winnerShipBoard;
    }
}
