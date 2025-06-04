package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Shield extends Component implements Activatable{
    private boolean active;

    public Shield(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    @VisibleForTesting
    public Shield(Map<Direction, Connector> connectors) {
        super(connectors);
        this.active = false;
    }

    public Set<Direction> getDefensibleDirections() {
        return Set.of(getOrientation(), getOrientation().getRight());
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
    public void addToVisitor(ComponentVisitor visitor) {
        visitor.add(this);
    }

    @Override
    public void removeFromVisitor(ComponentVisitor visitor) {
        visitor.remove(this);
    }
}
