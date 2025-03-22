package it.polimi.ingsw.galaxytruckers.shipBuilding;

import javafx.scene.image.Image;

import java.util.List;

public class Cannon extends Component {

    public Cannon(Image image, List<Connector> connectors) {
        super(image, connectors);
    }

    // TODO: consider renaming in getFirePowerHalves()
    public int getFirePower() {
        return (getOrientation() == 0) ? 2 : 1;
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
