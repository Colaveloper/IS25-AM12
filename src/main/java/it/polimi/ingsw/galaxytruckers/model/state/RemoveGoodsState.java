package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class RemoveGoodsState extends AdventureState implements GameStateInterface{
    private int goodsToLose;
    private final ShipBoard shipBoard;
    private GoodsType mostValuableGood;

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
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            while (goodsToLose > 0) {
                if (mostValuableGood != null) {
                    shipBoard.getCargoHolds().keySet().stream()
                            .filter(p -> shipBoard.getCargoHolds().get(p).getGoods().containsKey(mostValuableGood))
                            .findAny()
                            .ifPresent(p -> shipBoard.removeGoods(p,mostValuableGood,1));
                    computeMostValuableGood();
                } else {
                    shipBoard.getBatteries().keySet().stream()
                            .filter(p -> shipBoard.getBatteries().get(p).getNumBatteries() > 0)
                            .findAny()
                            .ifPresent(shipBoard::useBatteries);
                }
                goodsToLose--;
                tryStateTransition();
            }
        }
    }

    @Override
    public synchronized void loseGood(ShipBoard shipBoard, Point position) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        computeMostValuableGood();
        if (mostValuableGood != null) {
            shipBoard.removeGoods(position, mostValuableGood, 1);
        } else {
            shipBoard.useBatteries(position);
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
            getNextState();
        }
    }

    public synchronized int getGoodsToLose() {
        return goodsToLose;
    }

    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }
}
