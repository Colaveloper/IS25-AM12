package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.*;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AbandonedShipCard extends AdventureCard {
    private final int flightDaysLoss;
    private final int creditPrize;
    private final int requiredCrew;
    private boolean accepted;
    private boolean acquired;

    public AbandonedShipCard (Image image, Level cardLevel, FlightBoard flightBoard,int creditPrize, int requiredCrew, int flightDaysLoss) {
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
            if (currentShipBoard != null && currentShipBoard.getCrewSize() >= requiredCrew) {
                return new ChoiceState();
            }

            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) { // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;
                nextStep();
            }
        }
        if (!acquired) {
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
}
