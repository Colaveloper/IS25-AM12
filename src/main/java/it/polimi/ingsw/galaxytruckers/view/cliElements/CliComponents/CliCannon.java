package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cannon;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.util.List;

public class CliCannon extends CliComponent {

    private Cannon cannon;
    private static List<String> symbols = List.of("△", "▷", "▽", "◁");

    public CliCannon(ClientModel model, Cannon component) {
        super(model, component);
        this.cannon = component;
    }

    @Override
    public List<String> getDescription() {
        return generateborders(" " + symbols.get(cannon.getOrientation()) + " ");
    }
}

