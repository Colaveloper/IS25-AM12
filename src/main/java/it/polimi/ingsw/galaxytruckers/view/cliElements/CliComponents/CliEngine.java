package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Engine;

import java.util.List;

public class CliEngine extends CliComponent {

    private final Engine engine;
    private static final List<String> symbols = List.of("↓", "←", "↑", "→");

    public CliEngine(Engine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(engine.getOrientation()) + " ");
    }
}