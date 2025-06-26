package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.*;

public class AbandonedShipCard extends AdventureCard {
    private final int flightDaysLoss;
    private final int creditPrize;
    private final int requiredCrew;
    private boolean accepted;
    private boolean acquired;

    /**
     * Constructs an AbandonedShipCard.
     *
     * @param game the game instance
     * @param level the adventure card level
     * @param creditPrize the credits awarded for claiming the ship
     * @param requiredCrew the number of crew required to claim the ship
     * @param flightDaysLoss the number of flight days lost when claiming the ship
     * @param id the unique card identifier
     */
    public AbandonedShipCard (Game game, Level level, int creditPrize, int requiredCrew, int flightDaysLoss, int id) {
        super(game, level, id);
        this.flightDaysLoss = flightDaysLoss;
        this.creditPrize = creditPrize;
        this.requiredCrew = requiredCrew;
    }

    @Override
    public void initialize() {
        super.initialize();
        this.accepted = false;
        this.acquired = false;
    }

    @Override
    public AdventureState getNextState() {
        if(!accepted) {
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) { // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            }
            else {
                return new DrawCardState();
            }
            currentPlayerIndex++;
            if (currentShipBoard.getCrewSize() >= requiredCrew) {
                return new GrabRewardState(currentShipBoard, this::getReward);
            }
            else {
                return getNextState();
            }
        }
        else if(!acquired){
            acquired = true;
            return new RemoveCrewState(requiredCrew, currentShipBoard); // Let the player choose whether to collect the prize
        }
        return new DrawCardState();
    }

    /**
     * Method to apply the reward of the card, which consists of gaining credits and displacing the ship.
     */
    public void getReward() {
        currentShipBoard.gainCredits(creditPrize);
        flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
        accepted = true;
    }

    @VisibleForTesting
    public boolean getAccepted(){
        return this.accepted;
    }
}
