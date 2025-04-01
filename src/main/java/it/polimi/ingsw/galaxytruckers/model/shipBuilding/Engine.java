package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import javafx.scene.image.Image;

import java.util.List;

public class Engine extends Component {
    public Engine(Image image, List<Connector> connectors) {
        super(image, connectors);
    }

    public boolean isValid() {
        return getOrientation() == 0;
    }

    public int getEnginePower() {
        return 1;
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
