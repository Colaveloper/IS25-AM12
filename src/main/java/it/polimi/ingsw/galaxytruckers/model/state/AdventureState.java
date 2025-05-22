package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;

public class AdventureState extends GameState{
    @Override
    public void giveUp(ShipBoard shipBoard){
        if(game.getLevel() != Level.SECOND){
            throw new UnsupportedOperationException("This action is not admissible at the current game level: " + game.getLevel());
        }
        if(game.getGivenUpShips().contains(shipBoard)){
            throw new IllegalStateException("Ship has already given up");
        }
        game.forceShipToGiveUp(shipBoard);
    }
}
