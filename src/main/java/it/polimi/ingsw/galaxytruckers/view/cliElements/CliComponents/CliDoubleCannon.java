package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.DoubleCannon;

import java.util.List;
import java.util.Map;

/**
 * Represents a Double Cannon component in the CLI view of the Galaxy Truckers game.
 */
public class CliDoubleCannon extends CliComponent {

    private final DoubleCannon doubleCannon;

    /**
     * Constructs a CLI representation of a Double Cannon component.
     *
     * @param component the DoubleCannon component to be represented
     */
    public CliDoubleCannon(DoubleCannon component) {
        super(component);
        this.doubleCannon = component;
    }

    private static final Map<Direction, String> symbols = Map.of(
            Direction.UP, "▲",
            Direction.RIGHT, "▶",
            Direction.DOWN, "▼",
            Direction.LEFT, "◀"
    );


    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(doubleCannon.getOrientation()) + " ");
    }

}
