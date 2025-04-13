package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.DeclareEnginePowerState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

public class OpenSpaceCard extends AdventureCard {
    public OpenSpaceCard (Level level) {
        super(level);
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
