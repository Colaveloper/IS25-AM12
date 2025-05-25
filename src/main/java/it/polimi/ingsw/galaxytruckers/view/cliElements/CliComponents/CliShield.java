package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Shield;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.ArrayList;
import java.util.List;

public class CliShield extends CliComponent {

    private Shield shield;
    private static List<String> symbols = List.of("╮", "╯", "╰", "╭");

    public CliShield(ClientModel model, Shield shield) {
        super(model, shield);
        this.shield = shield;
    }

    @Override
    public List<String> getDescription() {
        return generateborders(" " + symbols.get(shield.getOrientation()) + " ");
    }
}
