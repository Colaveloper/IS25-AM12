package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state where players must remove goods from their ship in the Galaxy Truckers game.
 * This state is triggered when a ship encounters an event that requires sacrificing goods,
 * such as smugglers demanding cargo. The player must choose which
 * goods to remove from their ship until the required amount is met.
 */
public final class RemoveGoodsState extends AdventureState implements GameStateInterface, AdventureStateInterface {
    /** The number of goods that still need to be removed from the ship */
    private int goodsToLose;

    /**
     * Creates a new RemoveGoodsState with the specified parameters.
     * Initializes the state with the player's ship, the number of goods to lose,
     * and the currently active ship.
     *
     * @param myShip The ship board of the local player
     * @param goodsToLose The number of goods that must be sacrificed
     * @param currentShip The ship board that is currently active
     */
    public RemoveGoodsState(ShipBoard myShip, int goodsToLose, ShipBoard currentShip) {
        this.myShip = myShip;
        this.goodsToLose = goodsToLose;
        this.currentShip = currentShip;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(currentShip.equals(myShip)) actions.add(StateActions.LOSE_GOOD);
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        shipBoard.removeGoods(point, goodsType);
        goodsToLose--;
        game.getObservers().forEach(observer -> observer.notifyRemoveGoods(shipBoard, point, goodsType));
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        shipBoard.useBattery(point);
        goodsToLose--;
        game.getObservers().forEach(observer -> observer.notifyUseBattery(shipBoard, point));
    }

    /**
     * Gets the number of goods that still need to be removed.
     * This counter is decremented each time a good is successfully removed.
     * When it reaches zero, the player has satisfied the requirement.
     *
     * @return The remaining number of goods to lose
     */
    public int getGoodsToLose() {
        return goodsToLose;
    }
}
