package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.util.List;

public final class DoubleCannon extends Cannon implements Activatable {
    private boolean active;

    public DoubleCannon(List<Connector> connectors, int id) {
        super(connectors, id);
        this.active = false;
    }

    @Override
    public int getFirePower() {
        int power = this.active ? 2 : 0;
        if (getOrientation() == 0) return power*2;
        else return power;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
        notifyObservers();
    }

}
