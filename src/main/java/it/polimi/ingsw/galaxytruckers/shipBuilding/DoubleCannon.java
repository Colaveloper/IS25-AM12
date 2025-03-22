package it.polimi.ingsw.galaxytruckers.shipBuilding;

import javafx.scene.image.Image;

import java.util.List;

public class DoubleCannon extends Cannon implements Activatable {
    private boolean active;

    public DoubleCannon(Image image, List<Connector> connectors) {
        super(image, connectors);
        this.active = false;
    }

    @Override
    public int getFirePower() {
        int power = this.active ? 2 : 0;
        if (getOrientation() == 0) return power*2;
        else return power;
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
