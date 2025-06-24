package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public non-sealed class DoubleEngine extends Engine implements Activatable{
    private boolean active;

    public DoubleEngine(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    @VisibleForTesting
    public DoubleEngine(Map<Direction, Connector> connectors) {
        super(connectors);
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
    public void activate(ActivatableVisitor visitor) {
        this.active = true;
        visitor.activate(this);
    }

    @Override
    public void deactivate(ActivatableVisitor visitor) {
        visitor.deactivate(this);
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void addToVisitor(ComponentVisitor visitor, java.awt.Point point) {
        visitor.add(this, point);
    }

    @Override
    public void removeFromVisitor(ComponentVisitor visitor, java.awt.Point point) {
        visitor.remove(this, point);
    }
}
