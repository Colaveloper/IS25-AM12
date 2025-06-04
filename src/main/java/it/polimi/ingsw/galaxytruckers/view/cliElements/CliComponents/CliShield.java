package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Shield;

import java.util.List;
import java.util.Map;

public class CliShield extends CliComponent {

    private final Shield shield;
    private static final Map<Direction, String> symbols = Map.of(
            Direction.UP, "╮",
            Direction.RIGHT, "╯",
            Direction.DOWN, "╰",
            Direction.LEFT,"╭"
    );

    public CliShield(Shield shield) {
        super(shield);
        this.shield = shield;
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(shield.getOrientation()) + " ");
    }
}
