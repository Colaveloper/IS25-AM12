package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class OpenSpaceCard extends AdventureCard {
    public OpenSpaceCard (Image image, Level level, FlightBoard flightBoard) {
        super(image, level, flightBoard);
    }

    @Override
    public GameState nextStep() {
        if (currentShipBoard != null) {
            flightBoard.displaceShip(currentShipBoard, currentShipBoard.getEnginePower());
        }
        if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {  // There are other players to evaluate
            currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            currentPlayerIndex++;

            Set<Point> availablePositions = new HashSet<>(currentShipBoard.getEngines().keySet());
            availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
            return new ActivateState(availablePositions, currentShipBoard);
        }
        else {
            return new DrawCardState();
        }
    }
}
