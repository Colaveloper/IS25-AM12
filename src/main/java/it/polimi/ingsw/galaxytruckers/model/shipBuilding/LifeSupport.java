package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LifeSupport extends Component{
    private final CrewType crewType;

    public LifeSupport(Map<Direction, Connector> connectors, int id, CrewType crewType) {
        super(connectors, id);
        this.crewType = crewType;
        if (crewType == CrewType.HUMAN) {
            throw new IllegalArgumentException("LifeSupport type cannot be HUMAN, allowed types: " +
                    Arrays.stream(CrewType.values())
                            .filter(t -> !t.equals(CrewType.HUMAN))
                            .map(Object::toString)
                            .collect(Collectors.joining(",")));
        }
    }

    @VisibleForTesting
    public LifeSupport(Map<Direction, Connector> connectors, CrewType crewType) throws IllegalArgumentException {
        super(connectors);
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
