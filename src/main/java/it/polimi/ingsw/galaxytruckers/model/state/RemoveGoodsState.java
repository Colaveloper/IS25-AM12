package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Comparator;

public final class RemoveGoodsState extends AdventureState implements GameStateInterface{
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
    public synchronized void loseGood(ShipBoard shipBoard, Point position) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        computeMostValuableGood();
        if (mostValuableGood != null) {
            shipBoard.removeGoods(position, mostValuableGood, 1);
            game.getEventListener().notifyGoodsUpdateEvent(shipBoard, position, mostValuableGood, false);
        } else {
            shipBoard.useBatteries(position);
            game.getEventListener().notifyUseBatteryEvent(shipBoard, position);
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
