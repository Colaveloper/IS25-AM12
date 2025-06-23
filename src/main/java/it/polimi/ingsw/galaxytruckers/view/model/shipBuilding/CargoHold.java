package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a cargo hold component on the spaceship.
 */
public final class CargoHold extends Component {
    /** Maximum storage capacity of this cargo hold */
    private final int size;

    /** Map storing the quantity of each type of goods currently held */
    private final Map<GoodsType, Integer> goods;

    /** Indicates whether this is a special cargo hold */
    private final boolean isSpecial;

    /**
     * Constructs a new cargo hold component.
     *
     * @param connectors Map of directional connectors for this component
     * @param id Unique identifier for this component
     * @param isSpecial Whether this is a special cargo hold
     * @param size Maximum storage capacity of this cargo hold
     */
    public CargoHold(Map<Direction, Connector> connectors, int id, Boolean isSpecial, int size) {
        super(connectors, id);
        this.isSpecial = isSpecial;
        this.size = size;
        this.goods = new HashMap<>();
    }

    /**
     * Gets the maximum storage capacity of this cargo hold.
     *
     * @return The maximum number of goods units that can be stored
     */
    public int getSize() {
        return size;
    }

    /**
     * Checks if this is a special cargo hold.
     *
     * @return true if this is a special cargo hold, false otherwise
     */
    public boolean isSpecial() {
        return isSpecial;
    }

    /**
     * Gets a copy of the current goods inventory.
     *
     * @return A new map containing the current goods and their quantities
     */
    public Map<GoodsType, Integer> getGoods() {
        return new HashMap<>(goods);
    }

    /**
     * Adds one unit of a specific type of goods to the cargo hold.
     * If the goods type is not already present, it initializes the count to 1.
     *
     * @param goodsType The type of goods to add
     */
    public void addGoods(GoodsType goodsType) {
        if (!goods.containsKey(goodsType)) {
            goods.put(goodsType, 0);
        }
        goods.put(goodsType, goods.get(goodsType) + 1);
    }

    /**
     * Removes one unit of a specific type of goods from the cargo hold.
     * If the quantity reaches zero, the goods type is removed from the inventory.
     *
     * @param goodsType The type of goods to remove
     * @throws IllegalArgumentException if the specified goods type is not present
     */
    public void removeGoods(GoodsType goodsType) throws IllegalArgumentException {
        if (goods.containsKey(goodsType)) {
            goods.put(goodsType, goods.get(goodsType) - 1);
            if (goods.get(goodsType) == 0) {
                goods.remove(goodsType);
            }
        }
    }

    /**
     * Sets the entire goods inventory to a new state.
     * This replaces all current goods with the provided inventory.
     *
     * @param goods Map of goods types and their quantities to set
     */
    public void setGoods(Map<GoodsType, Integer> goods) {
        this.goods.clear();
        this.goods.putAll(goods);
    }
}
