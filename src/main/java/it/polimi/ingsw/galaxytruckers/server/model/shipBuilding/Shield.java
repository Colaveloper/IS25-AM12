package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;
import java.util.Set;

/**
 * Represents a shield component in the ship, which can be activated to defend against attacks.
 */
public non-sealed class Shield extends Component implements Activatable{
    private boolean active;

    /**
     * Constructs a Shield with the specified connectors and id.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     */
    public Shield(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    /**
     * Constructs a Shield with the specified connectors.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     */
    @VisibleForTesting
    public Shield(Map<Direction, Connector> connectors) {
        super(connectors);
        this.active = false;
    }

    public Set<Direction> getDefensibleDirections() {
        return Set.of(getOrientation(), getOrientation().getRight());
    }

    @Override
    public void activate() {
        this.active = true;
    }

    @Override
    public void deactivate() {
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }
}
