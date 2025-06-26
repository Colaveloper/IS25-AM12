package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;

public class SlaversCard extends AdventureCard {

    private final int firePowerThreshold;
    private final int creditPrize;
    private final int flightDaysLoss;
    private final int crewLoss;
    private boolean defeated;

    /**
     * Constructs a SlaversCard.
     *
     * @param game the game instance
     * @param level the adventure card level
     * @param crewLoss the number of crew lost if defeated
     * @param firePowerThreshold the firepower required to defeat the slavers
     * @param creditPrize the credits awarded for defeating the slavers
     * @param flightDaysLoss the number of flight days lost when claiming the prize
     * @param id the unique card identifier
     */
    public SlaversCard(Game game, Level level, int crewLoss, int firePowerThreshold, int creditPrize, int flightDaysLoss, int id) {
        super(game, level, id);
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.crewLoss = crewLoss;
    }

    @Override
    public void initialize() {
        super.initialize();
        defeated = false;
    }

    @Override
    public AdventureState getNextState() {
        // Evaluating previous player firepower, after double cannons activation
        if (!defeated) {
            if (currentShipBoard != null) {  // There is a previous player who needs their firepower evaluated
                int currentFirePower = currentShipBoard.getFirePower();
                currentShipBoard.deactivateAll();
                if (currentFirePower > firePowerThreshold) {  // player defeats the enemy
                    defeated = true;
                    return new GrabRewardState(currentShipBoard, this::getReward); // Let the player choose whether to collect the prize
                } else if (currentFirePower < firePowerThreshold) { // player is defeated
                    ShipBoard tempShipBoard = currentShipBoard;
                    currentShipBoard = null;
                    return new RemoveCrewState(crewLoss, tempShipBoard);
                }
            }
            // currentPlayer activate double cannons
            while (currentPlayerIndex < flightBoard.getOrderedShips().size()) { // find next player that hasn't given up
                ShipBoard nextShip = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;

                currentShipBoard = nextShip;
                return new DeclareFirePowerState(currentShipBoard); // Let the player activate double cannons
            }

            // no more players and no one has defeated the enemy
            defeated = false;
            return new DrawCardState();
        }
        else {
            return new DrawCardState();
        }
    }

    /**
     * Method to apply the reward of the card, which consists of gaining credits and displacing the ship.
     */
    public void getReward() {
        currentShipBoard.gainCredits(creditPrize);
        flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
        currentShipBoard = null;
    }
}
