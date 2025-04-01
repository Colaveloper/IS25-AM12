package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

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
