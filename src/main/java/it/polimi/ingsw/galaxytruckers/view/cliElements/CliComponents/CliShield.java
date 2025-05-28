package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Shield;

import java.util.List;

public class CliShield extends CliComponent {

    private final Shield shield;
    private static final List<String> symbols = List.of("╮", "╯", "╰", "╭");

    public CliShield(Shield shield) {
        super(shield);
        this.shield = shield;
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(shield.getOrientation()) + " ");
    }
}
