package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Comparator;

public class RemoveGoodsState extends GameState {
    int goodsToLose;
    ShipBoard shipBoard;
    GoodsType mostValuableGood;

    public RemoveGoodsState(int goodsToLose, ShipBoard shipBoard) {
        this.goodsToLose = goodsToLose;
        this.shipBoard = shipBoard;
        computeMostValuableGood();
    }

    @Override
    public void loseGood(Point position) {
        if (mostValuableGood != null) {
            shipBoard.removeGoods(position, mostValuableGood, 1);
            computeMostValuableGood();
        } else {
            shipBoard.useBatteries(position, 1);
        }
        goodsToLose--;
        if (goodsToLose == 0 || (shipBoard.getGoodsValue() == 0 && shipBoard.getNumBatteries() == 0)) {
            game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
        }
    }

    private void computeMostValuableGood() {
        mostValuableGood = shipBoard.getGoods().keySet().stream()
                .filter(g -> shipBoard.getGoods().get(g) > 0)
                .max(Comparator.comparingInt(GoodsType::getValue))
                .orElse(null);
    }
}
