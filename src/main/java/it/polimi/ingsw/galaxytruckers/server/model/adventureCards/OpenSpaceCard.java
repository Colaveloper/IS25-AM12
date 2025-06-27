package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.SurrenderPolicy;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.shared.enums.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DeclareEnginePowerState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DrawCardState;

public class OpenSpaceCard extends AdventureCard {

    /**
     * Constructs an OpenSpaceCard.
     *
     * @param game the game instance
     * @param level the adventure card level
     * @param id the unique card identifier
     */
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
