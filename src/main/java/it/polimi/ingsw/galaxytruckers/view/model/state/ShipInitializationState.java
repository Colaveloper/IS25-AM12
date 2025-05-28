package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ShipInitializationState extends GameState {
    private final Map<ShipBoard, Map<CrewType,List<Point>>> crewtypeToPoints;

    public ShipInitializationState(Map<ShipBoard, Map<CrewType, List<Point>>> crewtypeToPoints) {
        this.crewtypeToPoints = crewtypeToPoints;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        return List.of(
                StateActions.INITIALIZE_CABIN,
                StateActions.GO_NEXT
        );
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        shipBoard.initializeCabin(point, crewType);
        crewtypeToPoints.get(shipBoard).remove(crewType);
    }

    public Map<ShipBoard, Map<CrewType,List<Point>>> getCrewtypeToPoints() {
        return crewtypeToPoints;
    }
}
