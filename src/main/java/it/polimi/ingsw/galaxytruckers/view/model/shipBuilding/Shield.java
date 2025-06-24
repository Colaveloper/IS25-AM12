package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

/**
 * Represents a shield component on the spaceship. They implement the Activatable
 * interface to manage their power state.
 */
public final class Shield extends Component implements Activatable {
    private boolean active;

    public Shield(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

//    public int[] getDefensibleDirections() {
//        return new int[]{getOrientation(), (getOrientation()+1)%4};
//    }

    /**
     * Checks if the shield is currently activated.
     *
     * @return true if the shield is powered up, false otherwise
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Sets the activation state of the shield.
     *
     * @param active true to activate the shield's protection, false to deactivate
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

}
