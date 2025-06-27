package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

/**
 * Enum representing the types of connectors available for ship building.
 * Each connector type has specific matching rules with other connectors.
 */
public enum Connector {
    SINGLE, DOUBLE, UNIVERSAL, NONE;

    /**
     * Checks if this connector matches with another connector.
     *
     * @param other the other connector to match against
     * @return true if this connector matches with the other, false otherwise
     */
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
