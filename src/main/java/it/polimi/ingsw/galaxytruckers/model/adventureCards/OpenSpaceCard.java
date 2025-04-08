package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.model.state.DeclareEnginePowerState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class OpenSpaceCard extends AdventureCard {
    public OpenSpaceCard (Image image, Level level) {
        super(image, level);
    }

    @Override
    public GameState nextStep() {
        if (currentShipBoard != null) {
            flightBoard.displaceShip(currentShipBoard, currentShipBoard.getEnginePower());
        }
        if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {  // There are other players to evaluate
            currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            currentPlayerIndex++;
            return new DeclareEnginePowerState(currentShipBoard);
        }
        else {
            return new DrawCardState();
        }
    }
}
