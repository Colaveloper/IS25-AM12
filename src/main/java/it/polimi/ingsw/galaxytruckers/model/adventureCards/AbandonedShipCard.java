package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.GrabRewardState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.RemoveCrewState;

public class AbandonedShipCard extends AdventureCard {
    private final int flightDaysLoss;
    private final int creditPrize;
    private final int requiredCrew;
    private boolean accepted;
    private boolean acquired;

    public AbandonedShipCard (Level cardLevel, int creditPrize, int requiredCrew, int flightDaysLoss) {
        super(cardLevel);
        this.flightDaysLoss = flightDaysLoss;
        this.creditPrize = creditPrize;
        this.requiredCrew = requiredCrew;
    }

    @Override
    public void initialize(FlightBoard flightBoard) {
        super.initialize(flightBoard);
        this.accepted = false;
        this.acquired = false;
    }

    @Override
    public GameState nextStep() {
        if(!accepted) {
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) { // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            }
            else {
                return new DrawCardState();
            }
            currentPlayerIndex++;
            if (currentShipBoard.getCrewSize() >= requiredCrew) {
                return new GrabRewardState(this::getReward);
            }
            else {
                return nextStep();
            }
        }
        else if(!acquired){
            acquired = true;
            return new RemoveCrewState(requiredCrew, currentShipBoard); // Let the player choose whether to collect the prize
        }
        return new DrawCardState();
    }

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
