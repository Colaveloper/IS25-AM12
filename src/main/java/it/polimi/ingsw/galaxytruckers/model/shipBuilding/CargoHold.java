package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a cargo hold component in the ship, used to store goods of various types.
 */
public non-sealed class CargoHold extends Component implements ComponentInterface{
    private final int size;
    private int numGoods;
    private final Map<GoodsType, Integer> goods;
    private final boolean isSpecial;

    /**
     * Constructs a CargoHold with the specified connectors, id, special status, and size.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     * @param isSpecial whether this cargo hold can store special goods
     * @param size the capacity of the cargo hold
     */
    public CargoHold(Map<Direction, Connector> connectors, int id, boolean isSpecial, int size) {
        super(connectors, id);
        this.isSpecial = isSpecial;
        this.size = size;
        this.goods = new HashMap<>();
        this.numGoods = 0;
    }

    /**
     * Constructs a CargoHold with the specified connectors, size, and special status.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     * @param size the capacity of the cargo hold
     * @param isSpecial whether this cargo hold can store special goods
     */
    @VisibleForTesting
    public CargoHold(Map<Direction, Connector> connectors, int size, boolean isSpecial) {
        super(connectors);
        this.size = size;
        this.numGoods = 0;
        this.goods = new HashMap<>();
        this.isSpecial = isSpecial;
    }

    /**
     * Constructs a CargoHold with the specified size.
     * Used for testing purposes.
     *
     * @param size the capacity of the cargo hold
     */
    @VisibleForTesting
    public CargoHold(int size) {
        super();
        this.size = size;
        this.numGoods = 0;
        this.goods = new HashMap<>();
        this.isSpecial = false;
    }

    /**
     * @return true if this cargo hold can store special goods, false otherwise.
     */
    public boolean getIsSpecial() { return isSpecial; }

    /**
     * @return the goods stored in this cargo hold.
     */
    public Map<GoodsType, Integer> getGoods() {
        return new HashMap<>(goods);
    }

    /**
     * Sets the goods in the cargo hold.
     *
     * @param goods a map of goods types and their quantities
     */
    public void setGoods(Map<GoodsType, Integer> goods) {
        this.goods.clear();
        this.goods.putAll(goods);
        this.numGoods = goods.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Adds goods to the cargo hold.
     * @param goodsType the type of goods to add
     * @param amount the number of goods to add
     * @throws IllegalArgumentException if goods cannot be added to the cargo hold
     */
    public void addGoods(GoodsType goodsType, int amount) {
        if (goodsType == GoodsType.RED && !isSpecial) {
            throw new IllegalArgumentException("Cannot add special goods in non-special CargoHold");
        }
        int updatedNumGoods = this.numGoods + amount;
        if (updatedNumGoods > this.size) {
            throw new IllegalArgumentException("Cannot add the goods because total capacity would be exceeded");
        }
        goods.merge(goodsType, amount, Integer::sum);
        this.numGoods = updatedNumGoods;
    }

    /**
     * Removes goods from the cargo hold.
     * @param goodsType the type of goods to remove
     * @param amount the number of goods to remove
     * @throws IllegalArgumentException if there are not enough goods to remove
     */
    public void removeGoods(GoodsType goodsType, int amount) throws IllegalArgumentException {
        if (!goods.containsKey(goodsType) || goods.get(goodsType) < amount) {
            throw new IllegalArgumentException("Cannot remove the goods there are not enough");
        }
        goods.put(goodsType, goods.get(goodsType) - amount);
        if (goods.get(goodsType) <= 0) goods.remove(goodsType);
        this.numGoods -= amount;
    }
}
