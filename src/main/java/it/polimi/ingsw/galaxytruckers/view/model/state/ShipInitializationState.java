package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShipInitializationState extends GameState {
    private final Map<ShipBoard, Set<Point>> shipRelevantCabins = new HashMap<>();

    @Override
    public List<StateActions> getAvailableActions() {
        return List.of(
                StateActions.INITIALIZE_CABIN,
                StateActions.GO_NEXT
        );
    }

}
