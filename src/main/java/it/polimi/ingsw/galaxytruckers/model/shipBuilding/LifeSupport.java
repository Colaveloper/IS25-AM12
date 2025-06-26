package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a life support component in the ship, supporting a specific alien crew type.
 */
public non-sealed class LifeSupport extends Component implements ComponentInterface{
    private final CrewType crewType;

    /**
     * Constructs a LifeSupport with the specified connectors, id, and crew type.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     * @param crewType the type of crew supported by this life support
     */
    public LifeSupport(Map<Direction, Connector> connectors, int id, CrewType crewType) {
        super(connectors, id);
        this.crewType = crewType;
        checkCrewType();
    }

    /**
     * Constructs a LifeSupport with the specified connectors and crew type.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     * @param crewType the type of crew supported by this life support
     * @throws IllegalArgumentException if the crew type is HUMAN
     */
    @VisibleForTesting
    public LifeSupport(Map<Direction, Connector> connectors, CrewType crewType) throws IllegalArgumentException {
        super(connectors);
        this.crewType = crewType;
        checkCrewType();
    }

    /**
     * Constructs a LifeSupport with the specified crew type.
     * Used for testing purposes.
     *
     * @param crewType the type of crew supported by this life support
     */
    @VisibleForTesting
    public LifeSupport(CrewType crewType) {
        super();
        this.crewType = crewType;
        checkCrewType();
    }

    /**
     * Checks if the crew type is valid for a LifeSupport component.
     * Throws an IllegalArgumentException if the crew type is HUMAN
     */
    private void checkCrewType() {
        if (crewType == CrewType.HUMAN) {
            throw new IllegalArgumentException("LifeSupport type cannot be HUMAN, allowed types: " +
                    Arrays.stream(CrewType.values())
                            .filter(t -> !t.equals(CrewType.HUMAN))
                            .map(Object::toString)
                            .collect(Collectors.joining(",")));
        }
    }

    public CrewType getAlienType() {
        return crewType;
    }
}
