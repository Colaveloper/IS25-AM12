package it.polimi.ingsw.galaxytruckers.shared.enums;

/**
 * Enum representing the types of goods in the game.
 * Each type has a corresponding value that indicates its worth.
 */
public enum GoodsType {
    RED(4), YELLOW(3), GREEN(2), BLUE(1);

    private final int value;

    GoodsType(int value) {
        this.value = value;
    }

    /**
     * Gets the value associated with the goods type.
     *
     * @return the value of the goods type
     */
    public int getValue() {
        return value;
    }
}
