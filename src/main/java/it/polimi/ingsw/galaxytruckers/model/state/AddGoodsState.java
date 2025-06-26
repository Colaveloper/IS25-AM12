package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Map;

/**
 * Represents the state of the game where a player is adding goods to their ship board.
 * This state allows the player to add or remove goods from their ship board based on the
 * available goods in their buffer.
 */
public final class AddGoodsState extends AdventureState implements GameStateInterface {
    Map<GoodsType, Integer> goodsBuffer;
    ShipBoard shipBoard;

    /**
     * Constructor for AddGoodsState.
     *
     * @param goodsBuffer a map containing the types of goods and their quantities available to add
     * @param shipBoard   the ship board of the player who is adding goods
     */
    public AddGoodsState(Map<GoodsType, Integer> goodsBuffer, ShipBoard shipBoard) {
        this.goodsBuffer = goodsBuffer;
        this.shipBoard = shipBoard;
    }

    /**
     * Skips the current state if the given ship board matches the ship board adding goods.
     *
     * @param shipBoard the ship board of the player who wants to skip
     */
    @Override
    public void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            goNext(shipBoard);
        }
    }

    /**
     * {@inheritDoc}
     * Calls {@link ShipBoard#placeGoods(Point, GoodsType, int)} to add a good to the ship board.
     * Removes the added good from the buffer.
     *
     * @param shipBoard the ship board gaining the good
     * @param position  the position to add the good
     * @param good      the type of good to add
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void addGood(ShipBoard shipBoard, Point position, GoodsType good) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (goodsBuffer.containsKey(good) && goodsBuffer.get(good) > 0) {
            goodsBuffer.put(good, goodsBuffer.get(good) - 1);
            shipBoard.placeGoods(position, good, 1);
        } else {
            throw new IllegalArgumentException("You don't have goods of this type to add");
        }
    }

    /**
     * {@inheritDoc}
     * calls {@link ShipBoard#removeGoods(Point, GoodsType)} to remove a good from the ship board.
     * Adds the removed good to the buffer.
     *
     * @param shipBoard the ship board losing the good
     * @param position  the position of the good to remove
     * @param good      the type of good to remove
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void removeGood(ShipBoard shipBoard, Point position, GoodsType good) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        shipBoard.removeGoods(position, good);
        if (goodsBuffer.containsKey(good)) {
            goodsBuffer.put(good, goodsBuffer.get(good) + 1);
        } else {
            goodsBuffer.put(good, 1);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param shipBoard the ship board of the player that requested the action
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(this.shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        getNextState();
    }

    /**
     * @return the ship board of the player who is adding goods.
     */
    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }

    /**
     * @return the contents of the goods buffer, which contains the types of goods
     * and their quantities available to add.
     */
    public synchronized Map<GoodsType, Integer> getGoodsBuffer() {
        return goodsBuffer;
    }
}
