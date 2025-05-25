package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Shield;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.ArrayList;
import java.util.List;

public class CliShield extends CliComponent {

    private final Shield shield;
    private static final List<String> symbols = List.of("╮", "╯", "╰", "╭");

    public CliShield(Shield shield) {
        super(shield);
        this.shield = shield;
    }

    @Override
    public List<String> getDescription() {
        return generateborders(" " + symbols.get(shield.getOrientation()) + " ");
    }
}
