package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LifeSupport extends Component{
    private final CrewType crewType;

    public LifeSupport(Image image, List<Connector> connectors, CrewType crewType) throws IllegalArgumentException {
        super(image, connectors);
        this.crewType = crewType;
        if (crewType == CrewType.HUMAN) {
            throw new IllegalArgumentException("LifeSupport type cannot be HUMAN, allowed types: " +
                    Arrays.stream(CrewType.values())
                            .filter(t -> !t.equals(CrewType.HUMAN))
                            .map(Object::toString)
                            .collect(Collectors.joining(",")));
        }
    }

    public CrewType getAlienType() {
        return crewType;
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
