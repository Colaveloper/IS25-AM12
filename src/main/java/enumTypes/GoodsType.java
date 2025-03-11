package enumTypes;

public enum GoodsType {
    RED(4), YELLOW(3), GREEN(2), BLUE(1);

    private final int value;

    private GoodsType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
