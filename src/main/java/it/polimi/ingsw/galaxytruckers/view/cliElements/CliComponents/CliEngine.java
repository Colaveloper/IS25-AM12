package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Engine;

import java.util.List;
import java.util.Map;

public class CliEngine extends CliComponent {

    private final Engine engine;
    private static final Map<Direction, String> symbols = Map.of(
            Direction.UP, "↓",
            Direction.RIGHT, "←",
            Direction.DOWN, "↑",
            Direction.LEFT,"→"
    );

    public CliEngine(Engine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(engine.getOrientation()) + " ");
    }
}