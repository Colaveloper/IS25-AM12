package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.util.Map;

/**
 * Represents a cabin component in the ship, which can house crew members of a specific type.
 */
public non-sealed class Cabin extends Component implements ComponentInterface{
    private CrewType crewType;
    private int numResidents;

    /**
     * Constructs a Cabin with the specified connectors and id.
     *
     * @param connectors the connectors for this component
     * @param id the unique identifier for this component
     */
    public Cabin(Map<Direction, Connector> connectors, int id) {
        super(connectors,id);
        this.crewType = CrewType.HUMAN;
        this.numResidents = 0;
    }

    /**
     * Constructs a Cabin with the specified connectors.
     * Used for testing purposes.
     *
     * @param connectors the connectors for this component
     */
    @VisibleForTesting
    public Cabin(Map<Direction, Connector> connectors) {
        super(connectors);
        this.crewType = CrewType.HUMAN;
        this.numResidents = 0;
    }

    /**
     * Constructs a Cabin with default connectors.
     * Used for testing purposes.
     */
    @VisibleForTesting
    public Cabin() {
        super();
        this.crewType = CrewType.HUMAN;
        this.numResidents = 0;
    }

    /**
     * Initializes the cabin with a specific crew type and sets the number of residents based on the crew type.
     *
     * @param crewType The type of crew that will occupy the cabin.
     */
    public void initialize(CrewType crewType) {
        this.crewType = crewType;
        if (this.crewType == CrewType.HUMAN) {
            this.numResidents = 2;
        } else {
            this.numResidents = 1;
        }
    }

    /**
     * Gets the number of residents in the cabin.
     *
     * @return the number of residents
     */
    public int getNumResidents() {
        return numResidents;
    }

    /**
     * Gets the type of crew of residents of the cabin.
     *
     * @return the crew type
     */
    public CrewType getCrewType() {
        return crewType;
    }

    /**
     * Sets the crew type of residents on the cabin.
     *
     * @param crewType the crew type to set
     */
    public void setCrewType(CrewType crewType) {
        this.crewType = crewType;
    }

    /**
     * Sets the number of residents in the cabin.
     *
     * @param numResidents the number of residents to set
     */
    public void setNumResidents(int numResidents) {
        this.numResidents = numResidents;
    }

    /**
     * Method to remove a resident from the cabin.
     */
    public void loseResidents() {
        if (this.numResidents <= 0) {
            throw new IllegalStateException("There are no more residents here");
        }
        this.numResidents--;
    }
}
