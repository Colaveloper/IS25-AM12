package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.ChoiceState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.RemoveCrewState;
import javafx.scene.image.Image;

public class AbandonedShipCard extends AdventureCard {
    private final int flightDaysLoss;
    private final int creditPrize;
    private final int requiredCrew;
    private boolean accepted;
    private boolean acquired;

    public AbandonedShipCard (Image image, Level cardLevel, FlightBoard flightBoard, int creditPrize, int requiredCrew, int flightDaysLoss) {
        super(image, cardLevel, flightBoard);
        this.flightDaysLoss = flightDaysLoss;
        this.creditPrize = creditPrize;
        this.requiredCrew = requiredCrew;
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
                return new ChoiceState();
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

    @Override
    public void choose(boolean choice) {
        if (choice) {
            currentShipBoard.gainCredits(creditPrize);
            flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
            accepted = true;
        }
    }

    @VisibleForTesting
    public boolean getAccepted(){
        return this.accepted;
    }
}
