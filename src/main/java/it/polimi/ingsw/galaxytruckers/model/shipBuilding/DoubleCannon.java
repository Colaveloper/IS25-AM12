package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

public non-sealed class DoubleCannon extends Cannon implements Activatable {
    private boolean active;

    public DoubleCannon(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    @VisibleForTesting
    public DoubleCannon(Map<Direction, Connector> connectors) {
        super(connectors);
        this.active = false;
    }

    @VisibleForTesting
    public DoubleCannon() {
        super();
        this.active = false;
    }

    @Override
    public int getFirePower() {
        int power = this.active ? 2 : 0;
        if (getOrientation() == Direction.UP) return power*2;
        else return power;
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
