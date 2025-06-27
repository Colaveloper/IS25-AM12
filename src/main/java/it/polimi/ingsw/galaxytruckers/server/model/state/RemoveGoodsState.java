package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Comparator;

/**
 * Represents the state of the game where a player must remove goods from their ship board.
 */
public final class RemoveGoodsState extends AdventureState implements GameStateInterface{
    private int goodsToLose;
    private final ShipBoard shipBoard;
    private GoodsType mostValuableGood;

    public RemoveGoodsState(int goodsToLose, ShipBoard shipBoard) {
        this.goodsToLose = goodsToLose;
        this.shipBoard = shipBoard;
        computeMostValuableGood();
    }

    /**
     * {@inheritDoc}
     * <p>Moves to the next state if the ship has no more goods or batteries to lose</p>
     * @param game the game to associate with this state
     */
    @Override
    public void setGame(Game game) {
        super.setGame(game);
        tryStateTransition();
    }

    /**
     * If it's the player's turn, it skips the current state by removing goods or batteries.
     * @param shipBoard the ship board of the player who wants to skip
     */
    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            while (goodsToLose > 0) {
                if (mostValuableGood != null) {
                    shipBoard.getCargoHolds().keySet().stream()
                            .filter(p -> shipBoard.getCargoHolds().get(p).getGoods().containsKey(mostValuableGood))
                            .findAny()
                            .ifPresent(p -> shipBoard.removeGoods(p,mostValuableGood));
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

    /**
     * Removes a good from the ship board at the specified position by
     * calling {@link ShipBoard#removeGoods(Point, GoodsType)}. If no goods
     * are left, it removes batteries instead by calling {@link ShipBoard#useBatteries(Point)}.
     *
     * @param shipBoard the ship board losing the good
     * @param position  the position of the good to remove
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void loseGood(ShipBoard shipBoard, Point position) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        computeMostValuableGood();
        if (mostValuableGood != null) {
            try {
                shipBoard.removeGoods(position, mostValuableGood);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("You have to remove the most valuable good first: " + mostValuableGood.name());
            }
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

    /**
     * @return the number of goods that still need to be removed.
     */
    public synchronized int getGoodsToLose() {
        return goodsToLose;
    }

    /**
     * @return the ship board of the player whose turn it is.
     */
    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }
}
