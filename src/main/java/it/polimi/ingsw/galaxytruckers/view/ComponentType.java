package it.polimi.ingsw.galaxytruckers.view;

public enum ComponentType {
    CANNON("▲"),
    BATTERY("Θ"),
    ENGINE("⊓"),
    CABIN("●"),
    STORAGE("▞"),
    SHIELD("S"),
    LIFE_SUPPORT("Ѫ"),
    NONE("empty");

    private final String symbol;

    ComponentType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
