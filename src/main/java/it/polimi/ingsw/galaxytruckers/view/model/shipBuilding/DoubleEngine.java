package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

public final class DoubleEngine extends Engine implements Activatable {
    private boolean active;

    public DoubleEngine(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    @Override
    public int getEnginePower() {
        if (this.active) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

}
