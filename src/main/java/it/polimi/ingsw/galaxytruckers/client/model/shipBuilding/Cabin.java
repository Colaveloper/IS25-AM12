package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * Represents a cabin component on the spaceship that can house crew members.
 */
public final class Cabin extends Component {
    /** The type of crew assigned to this cabin (HUMAN or a kind of ALIEN) */
    private CrewType crewType;

    /** The current number of crew members residing in this cabin */
    private int numResidents;
    /**
     * Constructs a new empty cabin component.
     * Initially sets the crew type to HUMAN with no residents.
     *
     * @param connectors Map of directional connectors for this component
     * @param id Unique identifier for this component
     */
    public Cabin(Map<Direction, Connector> connectors, int id) {
        super(connectors,id);
        this.crewType = CrewType.HUMAN;
        this.numResidents = 0;
    }

    /**
     * Gets the type of crew assigned to this cabin.
     *
     * @return The crew type (HUMAN or ALIEN)
     */
    public CrewType getCrewType() {
        return crewType;
    }

    /**
     * Gets the current number of crew members in the cabin.
     *
     * @return The number of residents
     */
    public int getNumResidents() {
        return numResidents;
    }

    /**
     * Initializes the cabin with a specific crew type.
     * Sets the number of residents based on the crew type:
     * - Humans: 2 residents
     * - Aliens: 1 resident
     *
     * @param crewType The type of crew to assign to this cabin
     */
    public void initialize(CrewType crewType) {
        this.crewType = crewType;
        this.numResidents = switch (crewType) {
            case HUMAN -> 2;
            case PURPLE, BROWN -> 1;
            case null -> throw new IllegalArgumentException("CrewType is null");
        };
    }

    /**
     * Reduces the number of crew members in the cabin by one.
     * Called when a crew member is lost due to damage or other game events.
     */
    public void loseCrew() {
        this.numResidents--;
    }

    /**
     * Sets the type of crew assigned to this cabin.
     *
     * @param crewType The new crew type to assign
     */
    public void setCrewType(CrewType crewType) {
        this.crewType = crewType;
    }

    /**
     * Sets the number of crew members in the cabin.
     *
     * @param numResidents The new number of residents
     */
    public void setNumResidents(int numResidents) {
        this.numResidents = numResidents;
    }
}
