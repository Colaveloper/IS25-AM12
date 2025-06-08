package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Map;

public final class ShipInitializationState extends GameState {
    private final Map<ShipBoard, Map<CrewType,List<Point>>> crewtypeToPoints;

    public ShipInitializationState(ShipBoard myShip, Map<ShipBoard, Map<CrewType, List<Point>>> crewtypeToPoints) {
        this.myShip = myShip;
        this.crewtypeToPoints = crewtypeToPoints;
    }

    @Override
    public List<StateActions> getAvailableActions() {

        //no need to check for conditional actions, it's not possible to check if it's my turn so the check is in the screen
        return List.of(
                StateActions.INITIALIZE_CABIN,
                StateActions.GO_NEXT
        );
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        int numResidents = shipBoard.initializeCabin(point, crewType);
        crewtypeToPoints.get(shipBoard).remove(crewType);
        game.getObservers().forEach(observer -> observer.notifyInitializeCabin(shipBoard, point, crewType, numResidents));
    }

    public Map<ShipBoard, Map<CrewType,List<Point>>> getCrewtypeToPoints() {
        return crewtypeToPoints;
    }
}
