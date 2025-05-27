package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cannon;

import java.util.List;

public class CliCannon extends CliComponent {

    private final Cannon cannon;
    private static final List<String> symbols = List.of("△", "▷", "▽", "◁");

    public CliCannon(Cannon component) {
        super(component);
        this.cannon = component;
    }

    @Override
    public List<String> getNewDescription() {
        return addBorders(" " + symbols.get(cannon.getOrientation()) + " ");
    }
}

