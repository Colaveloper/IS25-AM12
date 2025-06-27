package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Shield;

import java.util.List;
import java.util.Map;

/**
 * Represents a CLI element that displays a shield component.
 */
public class CliShield extends CliComponent {

    private final Shield shield;
    private static final Map<Direction, String> symbols = Map.of(
            Direction.UP, "╮",
            Direction.RIGHT, "╯",
            Direction.DOWN, "╰",
            Direction.LEFT, "╭"
    );

    /**
     * Creates a new CLI shield from the given Shield model.
     *
     * @param shield the Shield model to create the CLI shield from
     */
    public CliShield(Shield shield) {
        super(shield);
        this.shield = shield;
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(shield.getOrientation()) + " ");
    }
}
