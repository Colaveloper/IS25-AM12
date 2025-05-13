package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Comparator;

public class RemoveGoodsState extends AdventureState {
    int goodsToLose;
    ShipBoard shipBoard;
    GoodsType mostValuableGood;

    public RemoveGoodsState(int goodsToLose, ShipBoard shipBoard) {
        this.goodsToLose = goodsToLose;
        this.shipBoard = shipBoard;
        computeMostValuableGood();
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        tryStateTransition();
    }

    @Override
    public void loseGood(ShipBoard shipBoard, Point position) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        if (mostValuableGood != null) {
            shipBoard.removeGoods(position, mostValuableGood, 1);
            computeMostValuableGood();
        } else {
            shipBoard.useBatteries(position, 1);
        }
        goodsToLose--;
        tryStateTransition();
    }

    private void computeMostValuableGood() {
        mostValuableGood = shipBoard.getGoods().keySet().stream()
                .filter(g -> shipBoard.getGoods().get(g) > 0)
                .max(Comparator.comparingInt(GoodsType::getValue))
                .orElse(null);
    }

    private void tryStateTransition() {
        if (goodsToLose == 0 || (shipBoard.getGoodsValue() == 0 && shipBoard.getNumBatteries() == 0)) {
            game.setCurrentState(game.getDeck().getCurrentCard().nextStep());
        }
    }
}
