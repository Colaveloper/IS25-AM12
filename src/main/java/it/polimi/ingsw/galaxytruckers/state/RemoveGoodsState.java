package it.polimi.ingsw.galaxytruckers.state;

import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.Map;

public class RemoveGoodsState extends GameState {
    int goods;
    ShipBoard shipBoard;

    public RemoveGoodsState(int goods, ShipBoard shipBoard) {
        this.goods = goods;
        this.shipBoard = shipBoard;
    }

    @Override
    public GameState getNextState() {
        return adventureCard.nextStep();
    }
}
