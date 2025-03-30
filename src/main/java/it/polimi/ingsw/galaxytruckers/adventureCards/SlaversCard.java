package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.*;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SlaversCard extends AdventureCard {

    private final int firePowerThreshold;
    private final int creditPrize;
    private final int flightDaysLoss;
    private final int crewLoss;
    private boolean defeated;

    protected SlaversCard(Image image, Level cardLevel, FlightBoard flightBoard, int crewLoss, int firePowerThreshold, int creditPrize, int flightDaysLoss) {
        super(image, cardLevel, flightBoard);
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.crewLoss = crewLoss;
    }

    @Override
    public GameState nextStep() {
        // Evaluating previous player firepower, after double cannons activation
        if (!defeated) {
            if (currentShipBoard != null) {  // There is a previous player who needs their firepower evaluated
                if (currentShipBoard.getFirePower() > firePowerThreshold) {  // player defeats the enemy
                    defeated = true;
                    return new ChoiceState(); // Let the player choose whether to collect the prize
                } else if (currentShipBoard.getFirePower() < firePowerThreshold) { // player is defeated
                    ShipBoard tempShipBoard = currentShipBoard;
                    currentShipBoard = null;
                    return new RemoveCrewState (crewLoss, tempShipBoard);
                }
            }
            // Letting the currentPlayer activate double cannons
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) { // There are other players to evaluate
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;
                Set<Point> availablePositions = new HashSet<>(currentShipBoard.getCannons().keySet());
                availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
                return new ActivateState(availablePositions, currentShipBoard); // Let the player activate double cannons
            } else {  // There are no more players and no one has defeated the enemy
                defeated = true;
                return new DrawCardState();
            }
        }
        else {
            return new DrawCardState();
        }
    }

    @Override
    public void choose(boolean choice) {
        if (choice) {
            currentShipBoard.gainCredits(creditPrize);
            flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
        }
        currentShipBoard = null;
    }
}
