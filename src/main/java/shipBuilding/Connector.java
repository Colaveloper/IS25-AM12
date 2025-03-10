package shipBuilding;

public enum Connector {
    SINGLE, DOUBLE, UNIVERSAL, NONE;

    public boolean matches(Connector other) {
        if (this == UNIVERSAL) {
            return other != NONE;
        } else if (this == NONE) {
            return other == NONE;
        } else {
            return other == this || other == UNIVERSAL;
        }
    }
}
