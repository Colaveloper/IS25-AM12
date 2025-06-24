package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the state where players can add goods to their ship in the Galaxy Truckers game.
 * This state allows players to place goods from a buffer onto their ship or remove goods
 * previously placed. Players can only interact with the goods during their turn.
 */
public final class AddGoodsState extends AdventureState {
    /** Buffer containing available goods that can be placed on the ship */
    private final GoodsBuffer goodsBuffer;

    /** Flag indicating whether it's the current player's turn */
    private final boolean isMyTurn;

    /**
     * Creates a new AddGoodsState with the specified parameters.
     * Initializes the state with the player's ship, a buffer of goods to place,
     * and the currently active ship.
     *
     * @param myShip The ship board of the local player
     * @param goodsBuffer Map of goods types to their quantities available for placement
     * @param currentShip The ship board that is currently active
     */
    public AddGoodsState(ShipBoard myShip, Map<GoodsType, Integer> goodsBuffer, ShipBoard currentShip) {
        this.myShip = myShip;
        this.isMyTurn = currentShip.equals(myShip);
        this.goodsBuffer = new GoodsBuffer(goodsBuffer);
        this.currentShip = currentShip;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(!isMyTurn) {
            return super.getAvailableActions();
        }
        if (!goodsBuffer.isEmpty()) {
            actions.add(StateActions.ADD_GOOD);
        }
        actions.add(StateActions.REMOVE_GOOD);
        actions.add(StateActions.GO_NEXT);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        goodsBuffer.removeFromBuffer(goodsType);
        shipBoard.placeGoods(point,goodsType);
        game.getObservers().forEach(observer -> observer.notifyPlaceGoods(shipBoard, point, goodsType));
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        goodsBuffer.addToBuffer(goodsType);
        shipBoard.removeGoods(point,goodsType);
        game.getObservers().forEach(observer -> observer.notifyRemoveGoods(shipBoard, point, goodsType));
    }

    /**
     * Gets the buffer of goods available for placement on the ship.
     * The goods buffer maintains the count of each type of good that can be placed.
     *
     * @return The goods buffer containing available goods
     */
    public GoodsBuffer getGoodsBuffer() {
        return goodsBuffer;
    }
}
