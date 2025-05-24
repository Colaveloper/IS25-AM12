package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.List;

public final class Shield extends Component implements Activatable {
    private boolean active;

    public Shield(List<Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    public int[] getDefensibleDirections() {
        return new int[]{getOrientation(), (getOrientation()+1)%4};
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
