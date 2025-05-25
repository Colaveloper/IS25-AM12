package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Engine;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Shield;

import java.util.List;

public class CliEngine extends CliComponent {

    private Engine engine;
    private static final List<String> symbols = List.of("↓", "←", "↑", "→");

    public CliEngine(ClientModel model, Engine engine) {
        super(model, engine);
        this.engine = engine;
    }

    @Override
    public List<String> getDescription() {
        return generateborders(" " + symbols.get(engine.getOrientation()) + " ");
    }
}