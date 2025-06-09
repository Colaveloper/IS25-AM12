package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cannon;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.DoubleCannon;

import java.util.List;
import java.util.Map;

public class CliDoubleCannon extends CliComponent {

    private final DoubleCannon doubleCannon;

    public CliDoubleCannon(DoubleCannon component) {
        super(component);
        this.doubleCannon = component;
    }

    private static final Map<Direction, String> symbols = Map.of(
            Direction.UP,   "▲",
            Direction.RIGHT,"▶",
            Direction.DOWN, "▼",
            Direction.LEFT, "◀"
    );


    @Override
    protected List<String> getNewDescription() {
        return addBorders(" " + symbols.get(doubleCannon.getOrientation()) + " ");
    }

}
