package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.SurrenderPolicy;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.DeclareEnginePowerState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;

public class OpenSpaceCard extends AdventureCard {

    public OpenSpaceCard (Game game, Level level, int id) {
        super(game, level, id);
    }

    @Override
    public AdventureState getNextState() {
        if (currentShipBoard != null) {
            if(currentShipBoard.getEnginePower() == 0) {
                SurrenderPolicy surrenderPolicy = game.getSurrenderPolicy();
                if (surrenderPolicy.isSurrenderEnabled()) {
                    surrenderPolicy.requestSurrender(currentShipBoard, SurrenderCause.NOENGINES);
                }
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
