package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.DeclareEnginePowerState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

public class OpenSpaceCard extends AdventureCard {
    public OpenSpaceCard (Game game, Level level) {
        super(game, level);
    }

    @Override
    public GameState nextStep() {
        if (currentShipBoard != null) {
            if(currentShipBoard.getEnginePower() == 0){
                game.forceShipToGiveUp(currentShipBoard);
                currentPlayerIndex++;
            }
            else{
                game.getFlightBoard().displaceShip(currentShipBoard, currentShipBoard.getEnginePower());
                currentShipBoard.deactivateAll();
            }
        }
        if (currentPlayerIndex < game.getFlightBoard().getShipToPlace().size()) {  // There are other players to evaluate
            currentShipBoard = game.getFlightBoard().getOrderedShips().get(currentPlayerIndex);
            currentPlayerIndex++;
            return new DeclareEnginePowerState(currentShipBoard);
        }
        else {
            return new DrawCardState();
        }
    }
}
